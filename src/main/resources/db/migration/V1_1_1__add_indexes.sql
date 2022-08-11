CREATE UNIQUE INDEX IF NOT EXISTS username_index ON app_user (username);
CREATE INDEX IF NOT EXISTS interval_from_index ON available_interval (from_time);
CREATE INDEX IF NOT EXISTS interval_until_index ON available_interval (until_time);
CREATE UNIQUE INDEX IF NOT EXISTS operator_user_index ON operator_info (user_id);