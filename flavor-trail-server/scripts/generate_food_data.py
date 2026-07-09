import json
import pymysql
from typing import List, Dict

provinces = [
    ("北京市", 1), ("天津市", 2), ("河北省", 3), ("山西省", 4), ("内蒙古自治区", 5),
    ("辽宁省", 6), ("吉林省", 7), ("黑龙江省", 8), ("上海市", 9), ("江苏省", 10),
    ("浙江省", 11), ("安徽省", 12), ("福建省", 13), ("江西省", 14), ("山东省", 15),
    ("河南省", 16), ("湖北省", 17), ("湖南省", 18), ("广东省", 19), ("广西壮族自治区", 20),
    ("海南省", 21), ("重庆市", 22), ("四川省", 23), ("贵州省", 24), ("云南省", 25),
    ("西藏自治区", 26), ("陕西省", 27), ("甘肃省", 28), ("青海省", 29), ("宁夏回族自治区", 30),
    ("新疆维吾尔自治区", 31), ("香港特别行政区", 32), ("澳门特别行政区", 33), ("台湾省", 34)
]

sample_foods = [
    {
        "name": "北京烤鸭",
        "description": "北京传统名菜，以果木炭火烤制，色泽红润，肉质细嫩，肥而不腻",
        "ingredients": ["鸭子", "荷叶饼", "甜面酱", "葱丝", "黄瓜"],
        "steps": ["处理鸭子", "烫皮挂糖", "晾皮", "烤制", "切片装盘"],
        "tags": ["北京菜", "传统名菜", "宴请"]
    },
    {
        "name": "麻婆豆腐",
        "description": "四川传统名菜，麻辣鲜香，豆腐嫩滑，牛肉末入味",
        "ingredients": ["豆腐", "牛肉末", "豆瓣酱", "花椒", "辣椒"],
        "steps": ["豆腐切块焯水", "炒牛肉末", "加调料煮", "勾芡出锅"],
        "tags": ["川菜", "麻辣", "家常菜"]
    },
    {
        "name": "西湖醋鱼",
        "description": "杭州传统名菜，选用草鱼，酸甜适口，色泽红亮",
        "ingredients": ["草鱼", "醋", "糖", "姜", "葱"],
        "steps": ["处理鱼", "蒸鱼", "调糖醋汁", "浇汁"],
        "tags": ["浙菜", "酸甜", "西湖"]
    },
    {
        "name": "宫保鸡丁",
        "description": "经典川菜，鸡肉嫩滑，花生酥脆，麻辣酸甜",
        "ingredients": ["鸡肉", "花生", "干辣椒", "花椒", "葱段"],
        "steps": ["腌制鸡肉", "炒干辣椒", "炒鸡肉", "加调料翻炒"],
        "tags": ["川菜", "家常菜", "下饭菜"]
    },
    {
        "name": "红烧肉",
        "description": "传统名菜，色泽红亮，肥而不腻，入口即化",
        "ingredients": ["五花肉", "冰糖", "酱油", "料酒", "姜"],
        "steps": ["焯水", "炒糖色", "炖煮", "收汁"],
        "tags": ["家常菜", "宴请", "经典"]
    }
]

def generate_foods_for_province(province_name: str, province_id: int) -> List[Dict]:
    result = []
    for i, food in enumerate(sample_foods):
        new_food = food.copy()
        new_food["province_id"] = province_id
        new_food["name"] = f"{province_name}{food['name'][:2]}" if i > 0 else food["name"]
        new_food["description"] = f"{province_name}特色{food['description']}"
        result.append(new_food)
    return result

def generate_sql() -> str:
    sql_lines = []
    for province_name, province_id in provinces:
        foods = generate_foods_for_province(province_name, province_id)
        for food in foods:
            ingredients = json.dumps(food["ingredients"], ensure_ascii=False)
            steps = json.dumps(food["steps"], ensure_ascii=False)
            tags = json.dumps(food["tags"], ensure_ascii=False)
            sql = f"""INSERT INTO `food` (`province_id`, `name`, `description`, `ingredients`, `steps_json`, `tags`, `status`)
VALUES ({food['province_id']}, '{food['name']}', '{food['description']}', '{ingredients}', '{steps}', '{tags}', 1);"""
            sql_lines.append(sql)
    return "\n\n".join(sql_lines)

if __name__ == "__main__":
    sql = generate_sql()
    with open("V3__seed_food.sql", "w", encoding="utf-8") as f:
        f.write(sql)
    print("SQL file generated successfully!")