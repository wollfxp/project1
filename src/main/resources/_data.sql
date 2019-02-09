ALTER DATABASE space
CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;
ALTER TABLE
space.users
  CONVERT TO CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

select * from users;
select * from mission;

insert into users (id, ai_cores, credits, hangar_size, name, premium)
values (1, 10, 15000, 10, 'admin', false);
insert into security_user (id, is_admin, password, username, user_id)
values (1, true, '111', 'admin', 1);

insert into starship_type (id, damage, damage_type, health, is_enemy_only, name, type, core_cost, credit_cost)
values (1, 3, 'THERMAL', 12, false, 'Alpha', 'alpha', 1, 1000);
insert into starship_type (id, damage, damage_type, health, is_enemy_only, name, type, core_cost, credit_cost)
values (2, 12, 'THERMAL', 16, false, 'Beta', 'beta', 1, 2500);
insert into starship_type (id, damage, damage_type, health, is_enemy_only, name, type, core_cost, credit_cost)
values (3, 4, 'THERMAL', 28, false, 'Gamma', 'gamma', 1, 3200);

insert into starship_type (id, damage, damage_type, health, is_enemy_only, name, type)
values (101, 1, 'THERMAL', 4, true, 'Drone', 'enemy-drone');
insert into starship_type (id, damage, damage_type, health, is_enemy_only, name, type)
values (102, 5, 'EXPLOSIVE', 9, true, 'Bomber', 'enemy-bomber');
insert into starship_type (id, damage, damage_type, health, is_enemy_only, name, type)
values (103, 7, 'THERMAL', 19, true, 'Lancer', 'enemy-lancer');
insert into starship_type (id, damage, damage_type, health, is_enemy_only, name, type)
values (104, 4, 'KINETIC', 31, true, 'Tank', 'enemy-tank');

insert into starship_type_resists (starship_type_id, resists, resists_key)
values (1, 0.4, 'THERMAL');
insert into starship_type_resists (starship_type_id, resists, resists_key)
values (1, 0.8, 'EXPLOSIVE');
insert into starship_type_resists (starship_type_id, resists, resists_key)
values (2, 0.3, 'THERMAL');
insert into starship_type_resists (starship_type_id, resists, resists_key)
values (2, 0.2, 'KINETIC');
insert into starship_type_resists (starship_type_id, resists, resists_key)
values (3, 0.4, 'KINETIC');
insert into starship_type_resists (starship_type_id, resists, resists_key)
values (3, 0.3, 'THERMAL');

insert into starship_type_resists (starship_type_id, resists, resists_key)
values (101, 0.7, 'EXPLOSIVE');
insert into starship_type_resists (starship_type_id, resists, resists_key)
values (101, 0.5, 'KINETIC');

insert into starship_type_resists (starship_type_id, resists, resists_key)
values (102, 0.7, 'KINETIC');
insert into starship_type_resists (starship_type_id, resists, resists_key)
values (102, 0.3, 'THERMAL');

insert into starship_type_resists (starship_type_id, resists, resists_key)
values (103, 0.4, 'KINETIC');
insert into starship_type_resists (starship_type_id, resists, resists_key)
values (103, 0.4, 'THERMAL');

insert into starship_type_resists (starship_type_id, resists, resists_key)
values (104, 0.8, 'KINETIC');
insert into starship_type_resists (starship_type_id, resists, resists_key)
values (104, 0.6, 'THERMAL');

insert into mission_type (id,
                          duration,
                          length,
                          level,
                          max_fleet_size,
                          name,
                          reward,
                          risk,
                          reward_cores_chance,
                          reward_cores_min,
                          reward_cores_max)
values (1, 60, 4, 1, 10, 'Convoy Escort', 500, 1, 0, 0, 0);
insert into mission_type (id,
                          duration,
                          length,
                          level,
                          max_fleet_size,
                          name,
                          reward,
                          risk,
                          reward_cores_chance,
                          reward_cores_min,
                          reward_cores_max)
values (2, 100, 1, 2, 15, 'Mining Ops Defense', 1300, 2, 0.1, 0, 1);
insert into mission_type (id,
                          duration,
                          length,
                          level,
                          max_fleet_size,
                          name,
                          reward,
                          risk,
                          reward_cores_chance,
                          reward_cores_min,
                          reward_cores_max)
values (3, 135, 2, 3, 8, 'Pirate Fleet Interception', 2200, 3, 0.3, 0, 1);
insert into mission_enemy_spawn (id, percentage, enemy_type_id)
values (1, 0.75, 101);
insert into mission_enemy_spawn (id, percentage, enemy_type_id)
values (2, 0.25, 102);

insert into mission_enemy_spawn (id, percentage, enemy_type_id)
values (3, 0.4, 101);
insert into mission_enemy_spawn (id, percentage, enemy_type_id)
values (4, 0.3, 102);
insert into mission_enemy_spawn (id, percentage, enemy_type_id)
values (5, 0.2, 103);
insert into mission_enemy_spawn (id, percentage, enemy_type_id)
values (6, 0.1, 104);

insert into mission_enemy_spawn (id, percentage, enemy_type_id)
values (7, 0.1, 101);
insert into mission_enemy_spawn (id, percentage, enemy_type_id)
values (8, 0.2, 102);
insert into mission_enemy_spawn (id, percentage, enemy_type_id)
values (9, 0.4, 103);
insert into mission_enemy_spawn (id, percentage, enemy_type_id)
values (10, 0.3, 104);

insert into mission_type_enemy_spawns (mission_type_id, enemy_spawns_id)
values (1, 1);
insert into mission_type_enemy_spawns (mission_type_id, enemy_spawns_id)
values (1, 2);
insert into mission_type_enemy_spawns (mission_type_id, enemy_spawns_id)
values (2, 3);
insert into mission_type_enemy_spawns (mission_type_id, enemy_spawns_id)
values (2, 4);
insert into mission_type_enemy_spawns (mission_type_id, enemy_spawns_id)
values (2, 5);
insert into mission_type_enemy_spawns (mission_type_id, enemy_spawns_id)
values (2, 6);

insert into mission_type_enemy_spawns (mission_type_id, enemy_spawns_id)
values (3, 7);
insert into mission_type_enemy_spawns (mission_type_id, enemy_spawns_id)
values (3, 8);
insert into mission_type_enemy_spawns (mission_type_id, enemy_spawns_id)
values (3, 9);
insert into mission_type_enemy_spawns (mission_type_id, enemy_spawns_id)
values (3, 10);