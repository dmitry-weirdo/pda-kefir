-- Скрипт удаления таблиц minstroy (utf-8)

-- ================= Тестовые сущности ========================
drop table Test_entity;
drop generator test_entity_gen;

drop table Choose_entity;
drop generator choose_entity_gen;

drop table Combo_box_entity;
drop generator combo_box_entity_gen;


-- ======================= ОБЩИЕ СПРАВОЧНИКИ  =============================
drop table Address;
drop generator address_gen;

drop table Attachment; 
drop generator attachment_gen;

drop table Dynamic_Grid;

commit;