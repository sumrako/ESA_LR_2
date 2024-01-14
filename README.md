# Лабораторная работа 2 (Spring)
Приложение лабораторной работы 1 было разработано для посетителей театра, которые хотят купить билет на представление онлайн. 

Это же приложение нацелено на **администраторов** (тех кто продает билет и регистрирует его в системе).

Кроме того, администраторам в этом случае предоставляются **расширенные права по управлению фильмами в прокате и расписанием сеансов**.

> Приложение реализовано с использованием фреймфорка Spring Boot, являющегося частью фреймворка Spring.
## PostgreSQL
В качестве СУБД была использована PostgreSQL. При установке postgres на локальный компьютер была создана локальная база данных, она и использовалась в работе.
Параметры подключения к базе данных и системные настройки проложения приведены в файле [application.proporties](https://github.com/vellarLa/ESA_LAB_2/blob/master/src/main/resources/application.properties)
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
**Предметная область - кинотеатр**. В качестве сущностей взяты:
- performance - спектакль
- timetable - расписание (зал, спектакль, время)
- ticket - билет

Схема базы данных:


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


## Спектакли
На этой странице мы видим список спектаклей, показывающихся в театре и их параметры. Здесь же есть форма для создания 
нового спектакля.


### Создание спектакля:

### Изменение спектакля. 
При нажатии кнопки "update" открывается форма для редактирования параметров спектакля

### Удаление спектакля.
При нажатии на кнопку "delete" спектакль удаляется. **Реализовано каскадное удаление. При удалении спектакля, удаляются все связанные с ним представления и купленные билеты.**

## Расписание
На этой странице мы видим список представлений. Здесь же есть форма для создания нового представления.


**В форме для создания представления в выпадающем списке отображаются все спектакли.**


### Изменение представдления. 
При нажатии кнопки "update" открывается форма для редактирования параметров представления


### Удаление представления. 
При нажатии на кнопку "delete" сеанс удаляется. **Реализовано каскадное удаление. При удалении представления, удаляются все купленные на него билеты.**

## Билеты
На этой странице мы видим список купленных билетов. Здесь же есть форма для бронирования билета.


### Каскад обновлений формы
В форме для бронирования билета в выпадающем списке отображаются все спектакли. 

**При выборе спектакля в следующем select будут доступны только его представления.**

**Также при изменении значений в select для расписания в select для выбора мест автоматически подтягиваются доступные для данного представления места (инициализация тоже происходит только доступными)**

### Изменение билета. 
При нажатии кнопки "update" открывается форма для редактирования параметров билета. В этой форме весь каскад обновлений также сохраняется.

До обновления

После обновления (обновили наличие льгот - поменялась стоимость)


