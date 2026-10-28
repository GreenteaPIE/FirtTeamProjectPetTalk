-- Pet Talk 데이터베이스 (MySQL 8)
-- 실행: mysql -u root -p < schema.sql
-- 코드(UserDAO·AdminDAO·MainFrame)가 실제로 쓰는 테이블·컬럼 순서에 맞춘 스키마

CREATE DATABASE IF NOT EXISTS Mini DEFAULT CHARACTER SET utf8mb4;
USE Mini;

-- 회원 (insert into Breeder values(id, pass, name, age, num, address))
CREATE TABLE IF NOT EXISTS Breeder (
  userId      VARCHAR(20) NOT NULL PRIMARY KEY,
  userPass    VARCHAR(20) NOT NULL,
  userName    VARCHAR(20) NOT NULL,
  userAge     INT,
  userNum     VARCHAR(20),
  userAddress VARCHAR(30)
);

-- 반려동물 (insert into Pet values(name, age, sex, breed, userId))
CREATE TABLE IF NOT EXISTS Pet (
  petName  VARCHAR(20) NOT NULL,
  petAge   INT NOT NULL,
  petSex   VARCHAR(10),
  petBreed VARCHAR(20) NOT NULL,
  userId   VARCHAR(20),
  FOREIGN KEY (userId) REFERENCES Breeder (userId) ON DELETE CASCADE
);

-- 친구 목록. 가입할 때 (fId=본인 아이디, userId=NULL) 한 줄이 들어가고, 친구를 추가하면 (fId=친구, userId=본인)
CREATE TABLE IF NOT EXISTS Friendlist (
  fId    VARCHAR(20) NOT NULL,
  userId VARCHAR(20),
  FOREIGN KEY (fId) REFERENCES Breeder (userId) ON DELETE CASCADE,
  FOREIGN KEY (userId) REFERENCES Breeder (userId) ON DELETE CASCADE
);

-- 관리자
CREATE TABLE IF NOT EXISTS admin (
  adminId VARCHAR(10) NOT NULL PRIMARY KEY,
  adminPw VARCHAR(20) NOT NULL
);
