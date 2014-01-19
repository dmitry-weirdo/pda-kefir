-- Скрипт создания таблиц kefir (utf-8)

-- ======================= ОБЩИЕ СПРАВОЧНИКИ  =============================
-- Ширина столбцов для каждого пользователя
create table Dynamic_Grid
(
	"order"         integer not null,
	login           varchar(32) not null,
	entity_name     varchar(255) not null,
	column_name     varchar(100) not null,
	width           integer not null,
	sort_dir        varchar(4),
	sort_order      smallint,
	group_id        varchar(255),
	hidden          smallint, -- 0 or 1,

	unique (login, entity_name, column_name)
);

-- Файловое приложение
create table Attachment
(
	id                 integer not null,
	entity_name        varchar(255) not null,
	entity_field_name  varchar(255) not null,
	entity_id          integer,

	file_name          varchar(255) not null,
	file_size          integer not null,
	content_type       varchar(255) not null,
	data               blob,

	primary key(id)
);
create generator attachment_gen;
set generator attachment_gen to 0;

-- Адрес (тестовая форма, может быть изменена по нуждам приложений)
create table Address
(
	id             integer not null,

	zip_code       integer,
	subject        varchar(51),
	district       varchar(50),
	city           varchar(50),
	city_district  varchar(50),
	locality       varchar(50),
	street         varchar(50),
	block          varchar(50), -- квартал
	house          varchar(15),
	building       varchar(5),
	apartment      varchar(5),

	primary key(id)
);
create generator address_gen;
set generator address_gen to 0;


-- ============================= Тестовые сущности ==============================
-- Сущность, выбираемая через комбобокс в форме тестовой сущности
create table Combo_box_entity
(
	id             integer not null,
	cadastral_number varchar(25) not null,

	primary key(id)
);
create generator combo_box_entity_gen;
set generator combo_box_entity_gen to 0;

-- Связанная сущность, выбираемая через свою форму выбора в форме тестовой сущности
create table Choose_entity
(
	id                    integer not null,
	name                  varchar(255) not null,
	short_name            varchar(255) not null,
	correspondent_account varchar(20),

	primary key(id)
);
create generator choose_entity_gen;
set generator choose_entity_gen to 0;

-- Тестовая сущность
create table Test_entity
(
	id                    integer not null,
	str_field             varchar(200),
	int_field             integer,
	int_spinner_field     integer,
	double_field          numeric(15, 2),
	date_field            date,
	boolean_field         smallint,
	enum_field            varchar(100),
	ogrn                  numeric(13, 0),
	inn                   numeric(12, 0),
	kpp                   numeric(15, 0),
	excluded_field        varchar(200),

	combo_box_entity_id   integer,
	choose_entity_id      integer,

	juridical_address_id  integer not null,
	physical_address_id   integer not null,

	primary key(id),
	foreign key(combo_box_entity_id) references Combo_box_entity(id),
	foreign key(choose_entity_id) references Choose_entity(id),
	foreign key(juridical_address_id) references Address(id),
	foreign key(physical_address_id) references Address(id)
);
create generator test_entity_gen;
set generator test_entity_gen to 0;

commit;