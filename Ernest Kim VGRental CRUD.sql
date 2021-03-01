--SHOW search_path;
SET search_path = vg_rentals;

--Create new user query (zip code must be >0 and < 100000) (User must be 18 or older)
--INSERT INTO users (username, password, date_of_birth, address, city, state, zip_code) 
--VALUES ('admin', 'changeTHISpassword', 'May 22, 1990', '99 Ness Rd.', 'Onett', 'IN', 45102) RETURNING vg_rentals.users.id;

--Update user profile
--UPDATE users SET username = 'rick' WHERE username = 'morty';

--Delete user query
--DELETE FROM users WHERE username = 'mr.meeseeks';

--Create new game entry
--INSERT INTO games (name, genre, console, publisher, developer, year_released) 
--VALUES ('Street Fighter 3: Third Strike', 'FIGHTING', 'DREAMCAST', 'Capcom', 'Capcom', 2000);

--Update game
--UPDATE games SET year_released = 1986 WHERE name = 'Chrono Trigger';
--UPDATE games SET available = false WHERE name = 'Sonic the Hedgehog 2';

--Delete game query
--DELETE FROM games WHERE name = 'Chrono Trigger';

--Create new rental
--INSERT INTO rentals (user_id, game_id)
--VALUES (22, 11);
--INSERT INTO rentals (user_id, game_id, due_date)
--VALUES (1, 1, 'February 12, 2021');

--Update rental
--UPDATE rentals SET due_date = 'February 12, 2021' WHERE user_id = 1 AND game_id = 1;
--UPDATE rentals set overdue = true WHERE game_id = 12;

--Delete rental query
DELETE FROM rentals WHERE user_id = 22 AND game_id = 11;
--DELETE FROM rentals WHERE user_id = 18 AND game_id = 8;

--Create new reviews query (rating must be between 1-10)
--INSERT INTO reviews (game_id, rating, review)
--VALUES (12, 9, 'Overall smooth fighter with some excellent mechanics');

--Update review
--UPDATE reviews SET rating = 9 WHERE id = 1;
--UPDATE reviews SET review = ' ' WHERE id = 1;

--Delete review query
--DELETE FROM reviews WHERE game_id = 1;

---------------------------------------------------------------------------------------------------------------

--User Profile
--SELECT username, date_of_birth, address, city, state, zip_code FROM users WHERE username = 'mr.meeseeks';

--Games rented
--SELECT name, console, publisher, due_date, overdue
--FROM rentals INNER JOIN games ON rentals.game_id = games.id WHERE rentals.user_id = 22;

--SELECT * FROM rentals WHERE user_id = 31 AND game_id = 10;
--Average rating for video game
--SELECT AVG(rating) FROM vg_rentals.reviews WHERE game_id = 9;

--Search
--SELECT * FROM games WHERE name LIKE '%Chr%';
--SELECT * FROM vg_rentals.games WHERE name LIKE '%Chrono%';

--Reviews page
--SELECT rating, review FROM reviews WHERE game_id = 1;