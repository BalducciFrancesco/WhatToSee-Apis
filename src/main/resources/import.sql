
# DON'T CHANGE INSERTS OR THEIR ORDER OF IMPORTS WITHOUT CHANGING EntityMock!

INSERT INTO city(name) VALUES ('Milano'), ('Roma'), ('Napoli');

INSERT INTO tag(name) VALUES ('Divertente'), ('Gratuito'), ('Accessibile'), ('Breve');

INSERT INTO theme(name) VALUES ('Montagna'), ('Panoramico'), ('Musei');

INSERT INTO user(dtype, first_name, last_name, password, username) VALUES ('Tourist', 'Mario', 'Rossi', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'm.rossi'); /* 123 */
INSERT INTO user(dtype, first_name, last_name, password, username) VALUES ('Tourist', 'Fernando', 'Azzurri', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'f.azzurri'); /* 123 */
INSERT INTO user(dtype, first_name, last_name, password, username) VALUES ('Guide', 'Rosario', 'Verdi', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'r.verdi'); /* 123 */
INSERT INTO user(dtype, first_name, last_name, password, username) VALUES ('Guide', 'Lorenzo', 'Grigi', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'l.grigi'); /* 123 */
INSERT INTO user(dtype, first_name, last_name, password, username) VALUES ('Administrator', 'Giovanni', 'Marroni', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'g.marroni'); /* 123 */

# TOUR 1 (REVIEWED, COMPLETED, REPORTED)
INSERT INTO tour(approx_cost, approx_duration, description, creation_date, is_public, title, guide_id, city_id, theme_id) VALUES (25.5, '07:30', 'Faremo un giro molto suggestivo', NOW(), TRUE, 'Giro suggestivo', 3, 1, 1);
INSERT INTO stop(cost, description, duration, title, transfer_cost, transfer_details, transfer_duration, transfer_other_options, transfer_type, tour_id) VALUES (5.50, 'Guardarsi attorno per ammirare la bellezza del luogo', '01:00', 'Visita a Piazza Duomo', 1.5, 'Prendere la linea 5A dalla stazione dei treni', '00:15', 'A piedi', 'Bus', 1);
INSERT INTO stop(cost, description, duration, title, transfer_cost, transfer_details, transfer_duration, transfer_other_options, transfer_type, tour_id) VALUES (1.50, 'Provare il gelato più buono di sempre', '00:30', 'Visita a gelateria da Nella', 1.5, 'Prendere la linea 5A dalla banchina', '02:15', 'Con monopattino', 'Monopattino', 1);
INSERT INTO tour_tags(tour_id, tag_id) VALUES (1, 1), (1, 2);
INSERT INTO review(description, stars, timestamp, tourist_id, tour_id) VALUES ('Molto suggestivo', 5, NOW(), 1, 1);
INSERT INTO completes(tour_id, tourist_id) VALUES (1, 1);
INSERT INTO report(description, tourist_id, tour_id) VALUES ('Potrebbe essere pericoloso per i bambini', 1, 1);

# TOUR 2 (NOT REVIEWED, NOT COMPLETED, NOT REPORTED)
INSERT INTO tour(approx_cost, approx_duration, description, creation_date, is_public, title, guide_id, city_id, theme_id) VALUES (5.5, '02:30', 'Ho provato a fare del mio meglio', NOW(), TRUE, 'Si fa quel che si può', 4, 3, 3);
INSERT INTO stop(cost, description, duration, title, transfer_cost, transfer_details, transfer_duration, transfer_other_options, transfer_type, tour_id) VALUES (5.50, 'Vedi un pò tu cosa fare', '09:30', 'Visita a quello che che si trova', 20.5, 'Prendere la linea retta e proseguire dopo la curva', '20:05', 'In astronave', 'Astronave', 2);

# TOUR 3 (NOT PUBLIC, NOT REVIEWED, COMPLETED, NOT REPORTED)
INSERT INTO tour(approx_cost, approx_duration, description, creation_date, is_public, title, guide_id, city_id, theme_id) VALUES (10.80, '01:00', 'Tutti invidieranno la tua scoperta', NOW(), FALSE, 'La parte nascosta di Roma', 3, 2, 2);
INSERT INTO stop(cost, description, duration, title, transfer_cost, transfer_details, transfer_duration, transfer_other_options, transfer_type, tour_id) VALUES (8.50, 'Osservare le piccole finestre', '02:00', 'Visita alla piazza dalle mille finestre', 2.5, 'Prendere la metro B direzione Rebibbia', '00:30', 'Con il tram A2', 'Metro', 3);
INSERT INTO tour_tags(tour_id, tag_id) VALUES (3, 3);
INSERT INTO shares(tour_id, tourist_id) VALUES (3, 1);
INSERT INTO completes(tour_id, tourist_id) VALUES (3, 1);
INSERT INTO completes(tour_id, tourist_id) VALUES (3, 2);   # should not appear in completions as it's not visible to him


INSERT INTO conversation(guide_id, tourist_id) VALUES (3, 1);
INSERT INTO message(content, direction, timestamp, conversation_id) VALUES ('Ciao, mi puoi dare più info su questo tour?', false, NOW(), 1);
INSERT INTO message(content, direction, timestamp, conversation_id) VALUES ('Certo, chiedi pure!', true, NOW(), 1);