ALTER TABLE login_history MODIFY login_agent VARCHAR(256) NOT NULL;
ALTER TABLE access_log MODIFY user_agent VARCHAR(256) NOT NULL;
