drop table if exists members cascade;
drop table if exists contacts cascade;
drop table if exists credentials cascade;
drop table if exists boards cascade;
drop table if exists articles cascade;
drop table if exists attached_files cascade;


create table members
(
	id serial primary key,
	korean_name text not null,
	english_name text not null,
	date_of_birth timestamp with time zone not null,
	email_address text not null unique,
	date_joined timestamp with time zone not null
);


create table contacts
(
	id serial primary key,
	member_id integer not null references members(id),
	home_address text not null,
	home_phone_number text not null,
	cell_phone_number text not null,
	last_update_time timestamp with time zone not null
);


create table credentials
(
	id serial primary key,
	email_address text not null references members(email_address),
	password text not null,
	member_id  integer not null references members(id),
	last_update_time  timestamp with time zone not null,
	activated boolean not null,
	expiration_date timestamp with time zone not null
);


create table boards
(
	id serial primary key,
	board_name text not null unique
);


create table articles
(
	id serial primary key,
	board_id integer not null references boards(id),
	member_id integer not null references members(id),
	article_number integer,
	title text,
	contents text not null,
	view_count integer,
	comment_of integer references articles(id),
	last_update_time  timestamp with time zone not null
);




create table attached_files
(
	id serial primary key,
	article_id integer not null references articles(id),
	file_path text not null unique
);


insert into members (korean_name, english_name, date_of_birth, email_address, date_joined)
values
	(
		'이진관',
		'Gene Kwan Lee', 
		to_timestamp('1980-11-17 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 
		'gene.kwan.lee@gmail.com', 
		now()
	);


insert into contacts (member_id, home_address, home_phone_number, cell_phone_number, last_update_time)
values
	(
		(select max(id) from members where email_address='gene.kwan.lee@gmail.com'),
		'134 Vlasis Dr. St. Louis MO 63011',
		'+1-314-898-2144',
		'+1-314-898-2144',
		now()
	);

	
insert into credentials (email_address, password, member_id, last_update_time, activated, expiration_date)
values
	(
		'gene.kwan.lee@gmail.com',
		'temptemp1234',
		(select max(id) from members where email_address='gene.kwan.lee@gmail.com'), 
		now(),
		TRUE,
		to_timestamp('2999-12-31 23:59:59', 'YYYY-MM-DD HH24:MI:SS')
	);
	

	
insert into members (korean_name, english_name, date_of_birth, email_address, date_joined)
values
	(
		'봉선화',
		'Sun Hwa Bong', 
		to_timestamp('1975-10-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 
		'bongsunhwa@test.com', 
		now()
	);


insert into contacts (member_id, home_address, home_phone_number, cell_phone_number, last_update_time)
values
	(
		(select max(id) from members where email_address='bongsunhwa@test.com'),
		'134 Vlasis Dr. St. Louis MO 63011',
		'+1-314-898-2144',
		'+1-314-898-2144',
		now()
	);

	
insert into credentials (email_address, password, member_id, last_update_time, activated, expiration_date)
values
	(
		'bongsunhwa@test.com',
		'temptemp1234',
		(select max(id) from members where email_address='bongsunhwa@test.com'), 
		now(),
		TRUE,
		to_timestamp('2999-12-31 23:59:59', 'YYYY-MM-DD HH24:MI:SS')
	);	



insert into boards (board_name)
values
	(
		'Temporary'
	);
	
	
insert into articles (board_id, member_id, article_number, title, contents, view_count, comment_of, last_update_time)
values
	(
		(select id from boards where board_name='Temporary'),
		(select id from members where email_address='gene.kwan.lee@gmail.com'),
		(select count(id) from articles where board_id=(select id from boards where board_name='Temporary'))+1,
		'첫번째 글',
		'이것은 첫번째 글내용입니다.',
		0,
		null,
		now()
	);
	
	
insert into articles (board_id, member_id, article_number, title, contents, view_count, comment_of, last_update_time)
values
	(
		(select id from boards where board_name='Temporary'),
		(select id from members where email_address='gene.kwan.lee@gmail.com'),
		(select count(id) from articles where board_id=(select id from boards where board_name='Temporary'))+1,
		'두번째 글',
		'이것은 두번째 글내용입니다.',
		0,
		null,
		now()
	);

	
insert into articles (board_id, member_id, article_number, title, contents, view_count, comment_of, last_update_time)
values
	(
		(select id from boards where board_name='Temporary'),
		(select id from members where email_address='gene.kwan.lee@gmail.com'),
		null,
		null,
		'이것은 첫번째 글의 코멘트 입니다.',
		null,
		(select min(id) from articles where board_id=(select id from boards where board_name='Temporary')),
		now()
	);
	
	
insert into articles (board_id, member_id, article_number, title, contents, view_count, comment_of, last_update_time)
values
	(
		(select id from boards where board_name='Temporary'),
		(select id from members where email_address='gene.kwan.lee@gmail.com'),
		null,
		null,
		'이것은 첫번째글 첫째 코멘트의 답변입니다.',
		null,
		(select min(id) from articles where comment_of=(select min(id) from articles where board_id=(select id from boards where board_name='Temporary'))),
		now()
	);

	
insert into attached_files(article_id, file_path)
values
	(
		(select a.id from articles a, boards b where a.board_id=b.id and board_name='Temporary' and article_number=1),
		'e:/mytemptemp.txt'
	);
	