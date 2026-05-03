-- 献立管理アプリ テーブル定義 (PostgreSQL)

-- 1. ユーザー管理テーブル
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. ユーザー目標設定テーブル
CREATE TABLE user_goals (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    target_calories INTEGER DEFAULT 2000,
    target_protein DECIMAL(5, 2) DEFAULT 0.0,
    target_fat DECIMAL(5, 2) DEFAULT 0.0,
    target_carbohydrates DECIMAL(5, 2) DEFAULT 0.0,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_user_goal UNIQUE (user_id)
);

-- 3. 献立親テーブル
CREATE TABLE meal_plans (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    plan_date DATE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_user_plan_date UNIQUE (user_id, plan_date)
);

-- 4. 食事テーブル (朝・昼・夕・間食)
CREATE TABLE meals (
    id SERIAL PRIMARY KEY,
    meal_plan_id INTEGER NOT NULL REFERENCES meal_plans(id) ON DELETE CASCADE,
    meal_type VARCHAR(20) NOT NULL,
    title VARCHAR(255) NOT NULL,
    total_calories INTEGER DEFAULT 0,
    protein DECIMAL(5, 2) DEFAULT 0.0,
    fat DECIMAL(5, 2) DEFAULT 0.0,
    carbohydrates DECIMAL(5, 2) DEFAULT 0.0,
    additional_nutrients JSONB, -- ID 05: 将来的な栄養素（塩分、ビタミン等）の拡張用
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_meal_per_plan UNIQUE (meal_plan_id, meal_type),
    CONSTRAINT check_meal_type CHECK (meal_type IN ('BREAKFAST', 'LUNCH', 'DINNER', 'SNACK'))
);

-- 5. 構成アイテムテーブル
CREATE TABLE meal_items (
    id SERIAL PRIMARY KEY,
    meal_id INTEGER NOT NULL REFERENCES meals(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    sort_order INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 6. タグテーブル
CREATE TABLE meal_tags (
    id SERIAL PRIMARY KEY,
    meal_id INTEGER NOT NULL REFERENCES meals(id) ON DELETE CASCADE,
    tag_name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- インデックス作成
CREATE INDEX idx_meal_plans_date ON meal_plans(plan_date);
CREATE INDEX idx_meal_plans_user ON meal_plans(user_id);
CREATE INDEX idx_meals_plan_id ON meals(meal_plan_id);

-- 更新日時自動更新用ファンクション
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- 各テーブルへのトリガー適用
DO $$
DECLARE
    t text;
BEGIN
    FOR t IN 
        SELECT table_name FROM information_schema.tables 
        WHERE table_schema = 'public' 
        AND table_name IN ('users', 'user_goals', 'meal_plans', 'meals', 'meal_items', 'meal_tags')
    LOOP
        EXECUTE format('DROP TRIGGER IF EXISTS update_timestamp_tg ON %I', t);
        EXECUTE format('CREATE TRIGGER update_timestamp_tg BEFORE UPDATE ON %I FOR EACH ROW EXECUTE PROCEDURE update_timestamp()', t);
    END LOOP;
END;
$$;
END;
$$;