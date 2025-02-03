DELIMITER $$
DROP PROCEDURE IF EXISTS loopInsert$$

CREATE PROCEDURE loopInsert()
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i <= 5000000  # 원하는 데이터의 개수 입력
        DO
            # 원하는 형태의 데이터를 토대로 INSERT 쿼리 작성
            INSERT INTO board(title, contents, writer_id)
            VALUES (concat('제목', i), concat('내용', i), concat('사용자', i));

            SET i = i + 1;
        END WHILE;
END$$
DELIMITER $$

# 작성한 MySQL Procedure 호출
CALL loopInsert;