create or replace function public.upd_db_modify_date_trg()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$ begin new.db_modify_date := now(); return new; end;  $function$
;

create trigger trg_upd_modify_date_data before update
on public.data for each row execute procedure public.upd_db_modify_date_trg();