USE [master];
GO

-- Drop Database only if it exists
DROP DATABASE IF EXISTS [fashion_store_management_system];
GO

-- Create new Database
CREATE DATABASE [fashion_store_management_system];
GO

-- Use Database
USE [fashion_store_management_system];
GO

-- ================================== CREATE TABLE ==================================
-- AUTHORITIES
CREATE TABLE [Roles] (
	[role_id] INT NOT NULL IDENTITY(1,1),
	[role_name] VARCHAR(50) NOT NULL DEFAULT 'ROLE_CUSTOMER',

	CHECK (
		[role_name] IN (
			'ROLE_CUSTOMER', 'ROLE_MANAGER', 'ROLE_ADMIN'
		)
	),

	PRIMARY KEY ([role_id]),
	UNIQUE ([role_name])
);

-- USERS
CREATE TABLE [Users] (
	[user_id] BIGINT IDENTITY(1000000,1),
	[email] VARCHAR(50) NOT NULL,
	[password] VARCHAR(255) NOT NULL,
	[full_name] VARCHAR(50) NOT NULL,
	[phone] VARCHAR(50),
	[home_address] VARCHAR(255),
	[avatar_url] VARCHAR(255),
	[is_enabled] BIT NOT NULL DEFAULT 1,
	[created_at] DATETIME NOT NULL DEFAULT GETDATE(),
	[updated_at] DATETIME NOT NULL DEFAULT GETDATE(),
	[role_id] INT DEFAULT NULL,

	PRIMARY KEY ([user_id]),
	UNIQUE ([email]),
	FOREIGN KEY ([role_id]) REFERENCES [Roles]([role_id])
);

-- CATEGORY
CREATE TABLE [Category] (
	[category_id] BIGINT IDENTITY(1,1) NOT NULL,
	[name] VARCHAR(100) NOT NULL,
	[description] VARCHAR(MAX),
	[created_at] DATETIME NOT NULL DEFAULT GETDATE(),
	[updated_at] DATETIME NOT NULL DEFAULT GETDATE(),

	PRIMARY KEY ([category_id]),
	UNIQUE ([name])
);



-- PRODUCT
CREATE TABLE [Product] (
	[product_id] BIGINT IDENTITY(1,1) NOT NULL,
	[name] VARCHAR(100) NOT NULL,
	[description] VARCHAR(MAX),
	[price] DECIMAL(10, 2) NOT NULL,
	[image_url] VARCHAR(255),
	[category_id] BIGINT NOT NULL,
	[stock_quantity] INT DEFAULT 0,
	[discount_percent] INT NOT NULL DEFAULT 0,
	[created_by] BIGINT NOT NULL,
	[created_at] DATETIME NOT NULL DEFAULT GETDATE(),
	[updated_at] DATETIME NOT NULL DEFAULT GETDATE(),

	CHECK ([stock_quantity] BETWEEN 0 AND 999),
	CHECK ([discount_percent] BETWEEN 0 AND 100),
	CHECK ([price] >= 0),

	PRIMARY KEY ([product_id]),
	FOREIGN KEY ([created_by]) REFERENCES [Users]([user_id]),
	FOREIGN KEY ([category_id]) REFERENCES [Category]([category_id])
);



-- ORDER
CREATE TABLE [Order] (
	[order_id] BIGINT IDENTITY(1,1) NOT NULL,
	[order_date] DATETIME NOT NULL DEFAULT GETDATE(),
	[total_amount] DECIMAL(10, 2) NOT NULL,
	[shipping_address] VARCHAR(255) NOT NULL,
	[receiver_name] VARCHAR(50) NOT NULL,
	[receiver_phone] VARCHAR(50) NOT NULL,
	[status] VARCHAR(50) NOT NULL DEFAULT 'PENDING',
	[ordered_by] BIGINT NOT NULL,
	[created_at] DATETIME NOT NULL DEFAULT GETDATE(),

	CHECK ([total_amount] >= 0.0),
	CHECK (
		[status] IN (
			'PENDING', 'CONFIRMED', 'SHIPPING', 'COMPLETED', 'CANCELLED'
		)
	),

	PRIMARY KEY ([order_id]),
	FOREIGN KEY ([ordered_by]) REFERENCES [Users]([user_id])
);



-- ORDER_DETAILS
CREATE TABLE [OrderDetails] (
	[order_detail_id] BIGINT IDENTITY(1,1) NOT NULL,
	[order_id] BIGINT NOT NULL,
	[product_id] BIGINT NOT NULL,
	[male_size] VARCHAR(10) NOT NULL DEFAULT 'L',
	[female_size] VARCHAR(10) NOT NULL DEFAULT 'L',
	[logo_text] VARCHAR(50),
	[logo_image] VARCHAR(255),
	[quantity] INT NOT NULL DEFAULT 1,
	[unit_price] DECIMAL(10, 2) NOT NULL,

	CHECK ([quantity] > 0),
	CHECK ([unit_price] >= 0.0),
	CHECK (
		[female_size] IN (
			'XS', 'S', 'M', 'L' , 'XL', '2XL', '3XL' 
		)
	),

	CHECK (
		[male_size] IN (
			'XS', 'S', 'M', 'L' , 'XL', '2XL', '3XL' 
		)
	),

	PRIMARY KEY ([order_detail_id]),
	FOREIGN KEY ([order_id]) REFERENCES [Order]([order_id]) ON DELETE CASCADE,
	FOREIGN KEY ([product_id]) REFERENCES [Product]([product_id])
);



-- PAYMENT
CREATE TABLE [Payment] (
	[payment_id] BIGINT IDENTITY(1,1) NOT NULL,
	[order_id] BIGINT NOT NULL,
	[amount] DECIMAL(10, 2) NOT NULL,
	[payment_method] VARCHAR(100) NOT NULL DEFAULT 'COD',
	[payment_status] VARCHAR(50) NOT NULL DEFAULT 'PENDING',
	[payment_date] DATETIME,
	[created_at] DATETIME NOT NULL DEFAULT GETDATE(),

	CHECK ([amount] >= 0.0),
	CHECK (
		[payment_method] IN 
			('COD', 'BANKING')
	),
	CHECK (
		[payment_status] IN
			('PENDING', 'PAID', 'FAILED', 'REFUNDED')
	),
	
	PRIMARY KEY ([payment_id]),
	FOREIGN KEY ([order_id]) REFERENCES [Order]([order_id]) ON DELETE CASCADE,
	UNIQUE ([order_id])
);

-- REVIEW 
CREATE TABLE [Review] (
	[rating_id] BIGINT IDENTITY(1,1),
	[product_id] BIGINT NOT NULL,
	[user_id] BIGINT NOT NULL,
	[order_detail_id] BIGINT NOT NULL,
	[comment] VARCHAR(MAX),
	[rating] INT NOT NULL,
	[created_at] DATETIME NOT NULL DEFAULT GETDATE(),

	CHECK ([rating] BETWEEN 1 AND 5),

	PRIMARY KEY ([rating_id]),
	FOREIGN KEY ([product_id]) REFERENCES [Product]([product_id]),
	FOREIGN KEY ([user_id]) REFERENCES [Users]([user_id]),
	FOREIGN KEY ([order_detail_id]) REFERENCES [OrderDetails]([order_detail_id]),
	UNIQUE ([order_detail_id])
) 


-- ========================================= SEED =========================================
INSERT INTO [Roles] ([role_name]) VALUES
	('ROLE_CUSTOMER'),
	('ROLE_MANAGER'),
	('ROLE_ADMIN');

INSERT INTO [Users] ([email], [password], [full_name], [phone], [home_address], [role_id]) VALUES
    ('admin@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'ADMINISTRATOR', '0123123123', 'Ho Chi Minh City', 3);

	INSERT INTO [Users]
([email], [password], [full_name], [phone], [home_address], [role_id])
VALUES
('nguyenvana@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'Nguyen Van A', '0901000001', 'Ho Chi Minh City', 1),
('tranthib@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'Tran Thi B', '0901000002', 'Ha Noi', 1),
('leminhc@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'Le Minh C', '0901000003', 'Da Nang', 1),
('phamthid@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'Pham Thi D', '0901000004', 'Can Tho', 1),
('hoange@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'Hoang E', '0901000005', 'Hai Phong', 1),

('vuf@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'Vu F', '0901000006', 'Hue', 1),
('dangg@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'Dang G', '0901000007', 'Quang Nam', 1),
('buih@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'Bui H', '0901000008', 'Binh Duong', 1),
('doti@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'Do Thi I', '0901000009', 'Dong Nai', 1),
('ngoj@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'Ngo J', '0901000010', 'Vung Tau', 1),

('phank@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'Phan K', '0901000011', 'Nha Trang', 1),
('truongl@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'Truong L', '0901000012', 'Da Lat', 1),
('lym@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'Ly M', '0901000013', 'Quy Nhon', 2),
('mainguyen@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'Mai Nguyen', '0901000014', 'Ben Tre', 1),
('voo@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'Vo O', '0901000015', 'Long An', 2),

('huynhp@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'Huynh P', '0901000016', 'Tien Giang', 1),
('caoq@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'Cao Q', '0901000017', 'Soc Trang', 1),
('luur@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'Luu R', '0901000018', 'Bac Lieu', 2),
('tons@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'Ton S', '0901000019', 'Ca Mau', 2),
('nguyent@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'Nguyen T', '0901000020', 'An Giang', 1);