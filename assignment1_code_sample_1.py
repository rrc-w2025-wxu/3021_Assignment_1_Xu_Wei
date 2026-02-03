import os
import pymysql
import subprocess
from urllib.request import urlopen
from urllib.error import URLError, HTTPError
from urllib.request import urlopen

db_config = {
    'host': os.getenv('DB_HOST'),
    'user': os.getenv('DB_USER'),
    'password': os.getenv('DB_PASSWORD')
}

def get_user_input():
    user_input = input('Enter your name: ').strip()

    if not user_input:
        raise ValueError("Name cannot be empty")

    if len(user_input) > 50:
        raise ValueError("Name is too long")

    return user_input

def send_email(to, subject, body):
    subprocess.run(['mail', '-s', subject, to], input=body.encode(), check=True)

def get_data():
    url = 'https://secure-api.com/get-data'  # 使用 HTTPS

    try:
        with urlopen(url, timeout=5) as response:
            data = response.read().decode('utf-8')
            return data
    except (HTTPError, URLError) as e:
        print(f"Error fetching data: {e}")
        return None

def save_to_db(data):
    query = "INSERT INTO mytable (column1, column2) VALUES (%s, %s)"

    try:
        connection = pymysql.connect(**db_config)
        cursor = connection.cursor()

        cursor.execute(query, (data, "Another Value"))
        connection.commit()

        print("Data saved to database safely.")

    except pymysql.MySQLError as e:
        print(f"Database error: {e}")

    finally:
        cursor.close()
        connection.close()

if __name__ == '__main__':
    user_input = get_user_input()
    data = get_data()
    save_to_db(data)
    send_email('admin@example.com', 'User Input', user_input)
