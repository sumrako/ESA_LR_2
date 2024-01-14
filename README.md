# Лабораторная работа 2 (Spring)
Приложение лабораторной работы 1 было разработано для посетителей театра, которые хотят купить билет на представление онлайн. 

Это же приложение нацелено на **администраторов** (тех кто продает билет и регистрирует его в системе).

Кроме того, администраторам в этом случае предоставляются **расширенные права по управлению фильмами в прокате и расписанием сеансов**.

> Приложение реализовано с использованием фреймфорка Spring Boot, являющегося частью фреймворка Spring.
## PostgreSQL
В качестве СУБД была использована PostgreSQL. При установке postgres на локальный компьютер была создана локальная база данных, она и использовалась в работе.
Параметры подключения к базе данных и системные настройки проложения приведены в файле [application.properties](https://github.com/sumrako/ESA_LAB_2/blob/master/src/main/resources/application.properties)
```bash
spring.datasource.hikari.connectionTimeout=20000
spring.datasource.hikari.maximumPoolSize=5

spring.datasource.url=jdbc:postgresql://localhost:5432/lab2
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto = update
spring.jpa.properties.hibernate.show_sql=true

spring.mvc.hiddenmethod.filter.enabled=true
server.port=8081
```
## Data layer
> Для сокрытия деталей реализации хранения сущностей был смоделирован дополнительный слой DTO.
Для работы с базой данный был смоделирован DAO слой.
> 
**Предметная область - театр**. В качестве сущностей взяты:
- performance - спектакль
- timetable - расписание (зал, спектакль, время)
- ticket - билет


## Business layer
Логика приложения реализована в Service классах. В бизнес-слое реализована логика поиска свободных мест (ряд и место) с учетом выбора сеанса.

Пример сервисного класса для работы с расписанием.

```bash
@Service
@RequiredArgsConstructor
public class TimetableServiceImpl implements TimetableService {
    private final TimetableRepository timetableRepository;
    @Override
    public void save(TimetableDto timetableDto) {
        timetableRepository.save(timetableDto.toEntity());
    }
    @Override
    public void deleteById(Long id) {
        timetableRepository.deleteById(id);
    }
    @Override
    public void deleteAll() {
        timetableRepository.deleteAll();
    }
    @Override
    public TimetableDto findById(Long id) {
        return timetableRepository.findFirstById(id).toDto();
    }
    @Override
    public List<TimetableDto> findAll() {
        return timetableRepository.findAll().stream().map(Timetable::toDto).collect(Collectors.toList());
    }
    @Override
    public List<TimetableDto> findAllByPerformance(PerformanceDto performanceDto) {
        return timetableRepository.findAllByPerformance_Id(performanceDto.getId())
                .stream().map(Timetable::toDto)
                .collect(Collectors.toList());
    }
}
```
## View layer
За обработку запросов отвечают контроллеры, пользовательский интерфейс реализован на html страницах.
# Демонстрация работоспособности
Стартовая страница. На ней можно выбрать, какую сущность будем модифицировать.
![image](https://github.com/sumrako/ESA_LR_2/assets/67976572/3cc51647-f299-4aaa-bde4-9da640761e66)


## Спектакли
На этой странице мы видим список спектаклей, показывающихся в театре и их параметры. Здесь же есть форма для создания 
нового спектакля.
![image](https://github.com/sumrako/ESA_LR_2/assets/67976572/80b5ab7c-1bff-4f2e-a56e-4294e97847c9)


### Создание спектакля:
![image](https://github.com/sumrako/ESA_LR_2/assets/67976572/da9ee343-d71d-406b-9d8b-0eb6e7d2396a)

### Изменение спектакля. 
При нажатии кнопки "update" открывается форма для редактирования параметров спектакля
![image](https://github.com/sumrako/ESA_LR_2/assets/67976572/5d4a617a-e5df-45ed-baeb-9ed5b98a01ed)

### Удаление спектакля.
При нажатии на кнопку "delete" спектакль удаляется. **Реализовано каскадное удаление. При удалении спектакля, удаляются все связанные с ним представления и купленные билеты.**
![image](https://github.com/sumrako/ESA_LR_2/assets/67976572/e53b3068-9953-4d3a-9e6f-ce763034be0c)

## Расписание
На этой странице мы видим список представлений. Здесь же есть форма для создания нового представления.
![image](https://github.com/sumrako/ESA_LR_2/assets/67976572/4c3d42ef-c6de-439c-864b-ab1d56a4e66e)


**В форме для создания представления в выпадающем списке отображаются все спектакли.**


### Изменение представления. 
При нажатии кнопки "update" открывается форма для редактирования параметров представления
![image](https://github.com/sumrako/ESA_LR_2/assets/67976572/11ccf41b-fc5d-4304-956a-21727cddfc7d)


### Удаление представления. 
При нажатии на кнопку "delete" представление удаляется. **Реализовано каскадное удаление. При удалении представления, удаляются все купленные на него билеты.**

## Билеты
На этой странице мы видим список купленных билетов. Здесь же есть форма для бронирования билета.
![image](https://github.com/sumrako/ESA_LR_2/assets/67976572/5a353313-0a9b-42b0-87a4-acb5558ba1a6)


### Каскад обновлений формы
В форме для бронирования билета в выпадающем списке отображаются все спектакли. 

**При выборе спектакля в следующем select будут доступны только его представления.**

**Также при изменении значений в select для расписания в select для выбора мест автоматически подтягиваются доступные для данного представления места (инициализация тоже происходит только доступными)**

### Изменение билета. 
При нажатии кнопки "update" открывается форма для редактирования параметров билета. В этой форме весь каскад обновлений также сохраняется.

![image](https://github.com/sumrako/ESA_LR_2/assets/67976572/ad4be7b1-1b06-41cf-bc9e-4e6c5bc562e7)

После обновления (обновили наличие льгот - поменялась стоимость)
![image](https://github.com/sumrako/ESA_LR_2/assets/67976572/5d28b1bd-ed88-44ad-b50e-0c82dc942b36)


