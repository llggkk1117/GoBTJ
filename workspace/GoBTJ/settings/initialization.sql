--drop table if exists members cascade;
--drop table if exists member_contacts cascade;
--drop table if exists logins cascade;
--drop table if exists privileges cascade;
--drop table if exists privilege_assignments cascade;
--drop table if exists boards cascade;
--drop table if exists articles cascade;
--drop table if exists files_attached_to_articles cascade;
--drop table if exists intercp_staff_positions cascade;
--drop table if exists intercp_branches cascade;
--drop table if exists intercp_counties cascade;
--drop table if exists intercp_work cascade;
--
--
--create table intercp_counties
--(
--	country_name text primary key
--);
--
--
--create table intercp_branches
--(
--	id serial primary key,
--	branch_name text not null unique,
--	country text not null references intercp_counties(country_name)
--);
--
--create table intercp_staff_positions
--(
--	position_name text primary key
--);
--
--create table boards
--(
--	id serial primary key,
--	board_name text not null unique
--);
--
--create table articles
--(
--	id serial primary key,
--	board integer not null references boards(id),
--	author integer not null references members(id),
--	title text not null,
--	contents text not null,
--	last_updated_time  timestamp with time zone not null
--);
--
--
--create table files_attached_to_articles
--(
--	id serial primary key,
--	article_id integer not null references articles(id),
--	file_name text not null,
--	file_location text not null
--);
--
--create table privileges
--(
--	privilege_name text primary key
--);
--
--create table privilege_accesses_type
--(
--	access_type text primary key
--);
--
--create table privilege_accesses
--(
--	id serial primary key,
--	privilege text not null references privileges(privilege_name),
--	board integer not null references boards(id),
--	access_type text not null references privilege_accesses_type(access_type)
--);
--
--create table privilege_assignments
--(
--	id serial primary key,
--	member_id integer not null references members(id),
--	privilege_id integer not null references privileges(id),
--	expiration_date  timestamp with time zone not null,
--	unique(member_id, privilege_id)
--);
--
--
create table members
(
	id serial primary key,
	korean_name text not null,
	english_name text not null,
	birthday date not null,
	email_address text not null unique,
	date_joined timestamp with time zone not null
);


create table member_contacts
(
	id serial primary key,
	member_id integer not null references members(id),
	home_address text not null,
	home_phone_number text not null,
	cell_phone_number text not null,
	last_updated_time timestamp with time zone not null
);


create table logins
(
	id serial primary key,
	member_id  integer not null references members(id),
	email_address text not null references members(email_address),
	password text not null,
	last_login_time timestamp with time zone not null,
	last_updated_time  timestamp with time zone not null
);
--
--
--
--
--create table intercp_work
--(
--	id serial primary key,
--	member_id integer not null references members(id),
--	position text not null references intercp_staff_positions(position_name),
--	branch integer not null references intercp_branches(id)
--);














insert into members (korean_name, english_name, birthday, email_address, date_joined) values ('이진관', 'Gene Kwan Lee', to_date('Nov-17-1980', 'Mon-DD-YYYY'), 'gene.kwan.lee@gmail.com', now());
insert into member_contacts (member_id, home_address, home_phone_number, cell_phone_number, last_updated_time)
values
(
	(select max(id) from members where email_address='gene.kwan.lee@gmail.com'),
	'home address',
	'home phone',
	'cell phone',
	now()
);
