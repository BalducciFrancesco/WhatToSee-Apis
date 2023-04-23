INSERT INTO city(name) VALUES ('Milano'), ('Roma'), ('Napoli');

INSERT INTO tag(name) VALUES ('Divertente'), ('Gratuito'), ('Accessibile'), ('Breve');

INSERT INTO theme(name) VALUES ('Montagna'), ('Panoramico'), ('Musei');

INSERT INTO user(dtype, first_name, last_name, password, username) VALUES ('Tourist', 'Mario', 'Rossi', '123', 'm.rossi');
INSERT INTO user(dtype, first_name, last_name, password, username) VALUES ('Guide', 'Rosario', 'Verdi', '123', 'r.verdi');
INSERT INTO user(dtype, first_name, last_name, password, username) VALUES ('Administrator', 'Giovanni', 'Marroni', '123', 'g.marroni');

INSERT INTO tour(approx_cost, approx_duration, description, creation_date, is_public, title, guide_id, city_id, theme_id) VALUES (25.5, '07:30', 'Faremo un giro molto suggestivo', NOW(), TRUE, 'Giro suggestivo', 2, 1, 1);
INSERT INTO stop(cost, description, duration, title, transfer_cost, transfer_details, transfer_duration, transfer_other_options, transfer_type, tour_id) VALUES (5.50, 'Guardarsi attorno per ammirare la bellezza del luogo', '01:00', 'Visita a Piazza Duomo', 1.5, 'Prendere la linea 5A dalla stazione dei treni', '00:15', 'A piedi', 'Bus', 1);
INSERT INTO tour_tags(tour_id, tag_id) VALUES (1, 1), (1, 2);

INSERT INTO review(description, stars, timestamp, tourist_id, tour_id) VALUES ('Molto suggestivo', 5, NOW(), 1, 1);
