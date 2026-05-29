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
	[user_id] BIGINT IDENTITY(1,1),
	[email] VARCHAR(50) NOT NULL,
	[password] VARCHAR(255) NOT NULL,
	[full_name] VARCHAR(50) NOT NULL,
	[phone] VARCHAR(50),
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


-- ========================================= TEST VALUES =========================================
INSERT INTO [Roles] ([role_name]) VALUES
	('ROLE_CUSTOMER'),
	('ROLE_MANAGER'),
	('ROLE_ADMIN');