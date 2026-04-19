-- Шаг 1

-- Возраст студента не может быть менее 16 лет
ALTER TABLE student
    ADD CONSTRAINT age_constraint CHECK (age>= 16);
-- Имена студентов должны быть уникальными и не равны нулю.
ALTER TABLE student
    ADD CONSTRAINT nameUnique TEXT UNIQUE NOT NULL (name),
-- Пара “значение названия” - “цвет факультета” должна быть уникальной.
 name VARCHAR(100) NOT NULL,
 color VARCHAR(50) NOT NULL,
  UNIQUE (name, color)
-- При создании студента без возраста ему автоматически должно присваиваться 20 лет.
ALTER TABLE student
    age INT DEFAULT 20

-- Шаг 2

CREATE TABLE human (
    id BIGINT
    name TEXT ,
    age INTEGER ,
    driver_licence BOOLEAN,
);

CREATE TABLE car (
    id BIGINT
    brend TEXT ,
    model VARCHAR ,
    price INTEGER
);

CREATE TABLE HUMAN_CAR_OWNER (
    id_human BIGING,
    id_car BIGINT
)

    ALTER TABLE HUMAN_CAR_OWNER
    ADD CONSTRAINT fk_human_human_car_owner
        FOREIGN KEY (id_human) REFERENCES human (id);

    ALTER TABLE HUMAN_CAR_OWNER
    ADD CONSTRAINT fk_car_human_car_owner
        FOREIGN KEY (id_car) REFERENCES car (id);


-- Шаг 3

-- Составить Join запрос. Получить только имя и возраст студента
SELECT student.name, student.age, faculty.name
FROM student
         JOIN faculty ON student.faculty_id = faculty.id;

-- Составить Join запрос. Получить только тех студентов, у которых есть аватарки.
SELECT student.name, student.age, faculty.name
FROM student
         JOIN faculty ON student.faculty_id = faculty.id
         JOIN avatar ON student.id = avatar.student_id;
