create table if not exists Player
(
    id        int auto_increment
        primary key,
    username  varchar(255)         not null,
    password  varchar(255)         not null,
    nickName  varchar(255)         null,
    avatar    varchar(255)         null,
    win       int        default 0 null,
    lose      int        default 0 null,
    draw      int        default 0 null,
    score     double     default 0 null,
    isOnline  tinyint(1) default 0 null,
    isPlaying tinyint(1) default 0 null
);

create table if not exists Product
(
    id      int auto_increment
        primary key,
    name    varchar(255) not null,
    amount  varchar(255) null,
    price   double       null,
    picture varchar(255) null
);

create table if not exists GameMatch
(
    id        int auto_increment
        primary key,
    player1ID int not null,
    player2ID int not null,
    winnerID  int null,
    productID int null,
    constraint GameMatch_ibfk_1
        foreign key (productID) references Product (id)
);

create index productID
    on GameMatch (productID);

INSERT INTO btl_ltm.Player (id, username, password, nickName, avatar, win, lose, draw, score, isOnline, isPlaying) VALUES (1, 'test001', 'Test@001', 'Nguyễn Văn A', null, 2, 1, 1, 1.5, 0, 0);
INSERT INTO btl_ltm.Player (id, username, password, nickName, avatar, win, lose, draw, score, isOnline, isPlaying) VALUES (2, 'test002', 'Test@002', 'Trần Thị B', null, 1, 2, 1, 0, 0, 0);
INSERT INTO btl_ltm.Player (id, username, password, nickName, avatar, win, lose, draw, score, isOnline, isPlaying) VALUES (3, 'test003', 'Test@003', 'Lê Văn C', null, 3, 0, 0, 3, 0, 0);
INSERT INTO btl_ltm.Player (id, username, password, nickName, avatar, win, lose, draw, score, isOnline, isPlaying) VALUES (4, 'test004', 'Test@004', 'Phạm Văn D', null, 0, 3, 0, -1.5, 0, 0);
INSERT INTO btl_ltm.Player (id, username, password, nickName, avatar, win, lose, draw, score, isOnline, isPlaying) VALUES (5, 'test005', 'Test@005', 'Hoàng Thị E', null, 1, 1, 2, 0.5, 0, 0);

INSERT INTO btl_ltm.Product (id, name, amount, price, picture) VALUES (1, 'Nước mắm Nam Ngư', '10 chai', 35000, null);
INSERT INTO btl_ltm.Product (id, name, amount, price, picture) VALUES (2, 'Bánh Trung Thu Kinh Đô', '5 hộp', 250000, null);
INSERT INTO btl_ltm.Product (id, name, amount, price, picture) VALUES (3, 'Cà phê G7', '20 gói', 50000, null);
INSERT INTO btl_ltm.Product (id, name, amount, price, picture) VALUES (4, 'Mì Hảo Hảo', '30 gói', 80000, null);
INSERT INTO btl_ltm.Product (id, name, amount, price, picture) VALUES (5, 'Nước ngọt Coca Cola', '12 lon', 150000, null);

INSERT INTO btl_ltm.GameMatch (id, player1ID, player2ID, winnerID, productID) VALUES (1, 1, 2, 1, 1);
INSERT INTO btl_ltm.GameMatch (id, player1ID, player2ID, winnerID, productID) VALUES (2, 1, 4, 1, 2);
INSERT INTO btl_ltm.GameMatch (id, player1ID, player2ID, winnerID, productID) VALUES (3, 1, 5, null, 3);
INSERT INTO btl_ltm.GameMatch (id, player1ID, player2ID, winnerID, productID) VALUES (4, 2, 3, 3, 4);
INSERT INTO btl_ltm.GameMatch (id, player1ID, player2ID, winnerID, productID) VALUES (5, 4, 3, 3, 5);
INSERT INTO btl_ltm.GameMatch (id, player1ID, player2ID, winnerID, productID) VALUES (6, 5, 3, 3, 1);
INSERT INTO btl_ltm.GameMatch (id, player1ID, player2ID, winnerID, productID) VALUES (7, 4, 5, 5, 2);
