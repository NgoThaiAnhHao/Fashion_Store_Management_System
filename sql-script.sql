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
CREATE TABLE [Orders] (
    [order_id] BIGINT IDENTITY(1,1) NOT NULL,
    [ordered_date] DATETIME NOT NULL DEFAULT GETDATE(),
    [total_amount] DECIMAL(10, 2) NOT NULL,
    [shipping_address] VARCHAR(255) NOT NULL,
    [receiver_name] VARCHAR(50) NOT NULL,
    [receiver_phone] VARCHAR(50) NOT NULL,
    [city] VARCHAR(50) NOT NULL,
    [zipcode] VARCHAR(50) NOT NULL,
    [status] VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    [reject_reason] VARCHAR(MAX),
    [ordered_by] BIGINT NOT NULL,

    CHECK ([total_amount] >= 0.0),
    CHECK (
    [status] IN (
     'PENDING',
     'LOGO_REJECTED',
     'CONFIRMED',
     'SHIPPING',
     'COMPLETED',
     'CANCELLED'
                )
    ),

    PRIMARY KEY ([order_id]),
    FOREIGN KEY ([ordered_by]) REFERENCES [Users]([user_id])
    );

-- ORDER_DETAILS
CREATE TABLE [Order_Details] (
    [order_detail_id] BIGINT IDENTITY(1,1) NOT NULL,
    [order_id] BIGINT NOT NULL,
    [product_id] BIGINT NOT NULL,

    [member1_size] VARCHAR(10) NOT NULL DEFAULT 'L',
    [member2_size] VARCHAR(10) NOT NULL DEFAULT 'L',

    [member1_gender] VARCHAR(20) NOT NULL DEFAULT 'OTHER',
    [member2_gender] VARCHAR(20) NOT NULL DEFAULT 'OTHER',

    [logo_text] VARCHAR(50),
    [logo_image_url] VARCHAR(100),
    [logo_size] VARCHAR(50), -- New field for logo size
    [logo_position] VARCHAR(50), -- New field for logo position

    [member1_quantity] INT NOT NULL DEFAULT 1,
    [member2_quantity] INT NOT NULL DEFAULT 1,

    [sub_total] DECIMAL(10, 2) NOT NULL,

    CHECK ([member1_quantity] >= 0),
    CHECK ([member2_quantity] >= 0),
    CHECK ([member1_quantity] > 0 OR [member2_quantity] > 0),

    CHECK ([sub_total] >= 0.0),

    CHECK (
    [member1_size] IN (
     'S', 'M', 'L', 'XL', '2XL', '3XL'
                      )
    ),

    CHECK (
    [member2_size] IN (
     'S', 'M', 'L', 'XL', '2XL', '3XL'
                      )
    ),

    CHECK (
    [member1_gender] IN (
     'MALE', 'FEMALE', 'OTHER'
                        )
    ),

    CHECK (
    [member2_gender] IN (
     'MALE', 'FEMALE', 'OTHER'
                        )
    ),

    PRIMARY KEY ([order_detail_id]),

    FOREIGN KEY ([order_id])
    REFERENCES [Orders]([order_id])
    ON DELETE CASCADE,

    FOREIGN KEY ([product_id])
    REFERENCES [Product]([product_id])
    );

-- PAYMENT
CREATE TABLE [Payment] (
    [payment_id] BIGINT IDENTITY(1,1) NOT NULL,
    [order_id] BIGINT NOT NULL,
    [total_amount] DECIMAL(10,2) NOT NULL,

    -- COD / CARD / PAYPAL
    [payment_method] VARCHAR(30) NOT NULL DEFAULT 'PAYPAL',

    -- PENDING / PAID / FAILED / REFUNDED
    [payment_status] VARCHAR(30) NOT NULL DEFAULT 'PENDING',

    -- PayPal
    [paypal_order_id] VARCHAR(255),
    [paypal_capture_id] VARCHAR(255),
    [created_at] DATETIME NOT NULL DEFAULT GETDATE(),

    PRIMARY KEY ([payment_id]),
    FOREIGN KEY ([order_id]) REFERENCES [Orders]([order_id])ON DELETE CASCADE,

    CHECK ([total_amount] >= 0),

    CHECK (
        [payment_method] IN ('COD', 'CARD', 'PAYPAL')
    ),

    CHECK (
        [payment_status] IN ('PENDING', 'PAID', 'FAILED', 'REFUNDED')
    )
);

-- NOTIFICATIONS
CREATE TABLE [Notifications] (
    [notification_id] BIGINT IDENTITY(1,1) NOT NULL,
    [user_id] BIGINT NOT NULL,
    [order_id] BIGINT NOT NULL,
    [title] NVARCHAR(255) NOT NULL,
    [message] NVARCHAR(MAX) NOT NULL,
    [type] VARCHAR(50) NOT NULL,
    [is_read] BIT NOT NULL DEFAULT 0,
    [created_at] DATETIME NOT NULL DEFAULT GETDATE(),

    PRIMARY KEY ([notification_id]),
    FOREIGN KEY ([user_id]) REFERENCES [Users]([user_id]),
    FOREIGN KEY ([order_id]) REFERENCES [Orders]([order_id])
);

-- ========================================= SEED =========================================
    INSERT INTO [Roles] ([role_name])
    VALUES
('ROLE_CUSTOMER'),
('ROLE_MANAGER'),
('ROLE_ADMIN');

INSERT INTO [Users] ([email], [password], [full_name], [phone], [home_address], [role_id])
VALUES
    ('admin@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'ADMINISTRATOR', '0123123123', 'Ho Chi Minh City', 3),
    ('manager@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'MANAGER', '0123123123', 'Ho Chi Minh City', 2),
    ('customer@gmail.com', '$2a$12$E2Is0z.gnHBYvE2OkohFQOkLZjLi5piuy3ZDzV2wmQF8A6ubH0fcO', 'CUSTOMER', '0123123123', 'Ho Chi Minh City', 1);

INSERT INTO [Users]([email], [password], [full_name], [phone], [home_address], [role_id])
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

INSERT INTO [Category] ([name], [description])
VALUES
    ('Couple T-Shirts','Matching t-shirts designed for couples with romantic and trendy styles.'),
    ('Family Matching','Coordinated shirts for families, perfect for vacations, gatherings, and special occasions.'),
    ('Best Friend Shirts','Fun and stylish matching shirts for best friends and close companions.'),
    ('Oversized Collection','Modern oversized matching t-shirts featuring a comfortable and fashionable fit.'),
    ('Seasonal Collection','Matching shirts designed for holidays and special events such as Valentine''s Day, Christmas, and New Year.');

INSERT INTO [Product]([name], [description], [price], [image_url], [category_id], [stock_quantity], [discount_percent], [created_by])
VALUES
-- Couple T-Shirts (Category 1)
    ('Forever Together Couple Tee',
    'Romantic matching t-shirt set for couples with a minimalist heart design.',
    24.99, 'images/products/couple-01.jpg', 1, 999, 10, 1000013),

    ('King & Queen Matching Shirt',
    'Stylish matching shirts featuring King and Queen prints.',
    29.99, 'images/products/couple-02.jpg', 1, 999, 15, 1000013),

    ('Love Story Couple T-Shirt',
    'Comfortable cotton couple shirt perfect for everyday wear.',
    22.50, 'images/products/couple-03.jpg', 1, 999, 5, 1000015),

    ('Soulmate Matching Tee',
    'Modern matching t-shirts designed for couples who love simplicity.',
    26.99, 'images/products/couple-04.jpg', 1, 999, 0, 1000015),

-- Family Matching (Category 2)
    ('Happy Family Vacation Shirt',
    'Matching family shirts ideal for vacations and family trips.',
    34.99, 'images/products/family-01.jpg', 2, 999, 20, 1000018),

    ('Family Squad T-Shirt',
    'Fun family matching shirt collection with squad-inspired design.',
    32.50, 'images/products/family-02.jpg', 2, 999, 10, 1000018),

    ('Family Day Matching Tee',
    'Soft cotton family t-shirts perfect for weekend gatherings.',
    28.99, 'images/products/family-03.jpg', 2, 999, 0, 1000019),

    ('Together We Shine Family Shirt',
    'Coordinated family shirt featuring a cheerful and modern look.',
    31.99, 'images/products/family-04.jpg', 2, 999, 12, 1000019),

-- Best Friend Shirts (Category 3)
    ('Besties Forever Shirt',
    'Matching best friend t-shirts designed for lifelong friendships.',
    21.99, 'images/products/bff-01.jpg', 3, 999, 10, 1000013),

    ('Partner In Crime Tee',
    'Fun matching shirt set for best friends with playful graphics.',
    23.99, 'images/products/bff-02.jpg', 3, 999, 5, 1000013),

    ('Friendship Goals T-Shirt',
    'Trendy matching shirt celebrating friendship and memories.',
    24.50, 'images/products/bff-03.jpg', 3, 999, 0, 1000015),

    ('Dynamic Duo Shirt',
    'Matching t-shirt for inseparable best friends.',
    22.99, 'images/products/bff-04.jpg', 3, 999, 8, 1000015),

-- Oversized Collection (Category 4)
    ('Urban Oversized Tee',
    'Premium oversized t-shirt with a modern streetwear design.',
    35.99, 'images/products/oversized-01.jpg', 4, 999, 15, 1000018),

    ('Minimalist Oversized Shirt',
    'Simple oversized t-shirt crafted for comfort and style.',
    33.99, 'images/products/oversized-02.jpg', 4, 999, 10, 1000018),

    ('Vintage Street Oversized Tee',
    'Fashionable oversized shirt featuring a retro-inspired print.',
    38.50, 'images/products/oversized-03.jpg', 4, 999, 20, 1000019),

    ('Classic Oversized Cotton Shirt',
    'Comfortable oversized cotton t-shirt suitable for all seasons.',
    30.99, 'images/products/oversized-04.jpg', 4, 999, 0, 1000019),

-- Seasonal Collection (Category 5)
    ('Valentine Love Matching Tee',
    'Special edition matching shirt designed for Valentine''s Day.',
    27.99, 'images/products/seasonal-01.jpg', 5, 999, 25, 1000013),

    ('Christmas Family Shirt',
    'Festive family matching shirt featuring Christmas-themed graphics.',
    36.99, 'images/products/seasonal-02.jpg', 5, 999, 30, 1000015),

    ('Happy New Year Matching Tee',
    'Celebrate the new year with coordinated matching shirts.',
    29.50, 'images/products/seasonal-03.jpg', 5, 999, 18, 1000018),

    ('Summer Beach Matching Shirt',
    'Lightweight matching shirt perfect for beach vacations and summer events.',
    28.99, 'images/products/seasonal-04.jpg', 5, 999, 10, 1000019);