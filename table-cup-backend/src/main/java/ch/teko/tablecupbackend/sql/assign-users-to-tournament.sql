insert into tournament_users (tournament_id, users_id)
select 1, id from user where lower(username) like 'testuser%' limit 36;