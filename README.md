# nutrition_app_java

このプロジェクトは栄養素情報を管理するためのアプリケーションです。

## 使用技術

- **フレームワーク**: Spring Boot (バージョン: 3.1.4)
- **ビルドツール**: Maven
- **データベース接続**: JPA (Spring Data JPA)
- **データベース**: PostgreSQL
- **テンプレートエンジン**: Thymeleaf
- **認証・認可**: Spring Security
- **テスティング**:
  - Spring Boot Starter Test
  - JUnit 5 (JUnit Jupiter Engine)
- **その他のライブラリ**:
  - Lombok (バージョン: 1.18.30)
  - Jakarta Persistence API

## 必要条件

- Java (バージョン: 1.8.0_381)
- Maven (バージョン: 3.9.5)
- PostgreSQL (バージョン: 16.0)
- pgAdmin4

## インストールとセットアップ

プロジェクトのセットアップ手順を詳細に説明します。以下は一般的な手順の例です。

- リポジトリをクローン

```git clone https://github.com/ayabe2000/nutrition_app_java.git```
```cd nutrition_app_java```

- データベースの設定

pgAdmin4をインストールし、新しいデータベースを作成します。公式サイトからダウンロードできます。

```https://www.postgresql.org/download/```

データベース名を my_project_db などの適切な名前に設定します。

src/main/resources/application.properties または application.yml ファイルを開き、以下の設定を編集します

```spring.datasource.url=jdbc:postgresql://localhost:5432/my_project_db```
```spring.datasource.username=YOUR_DB_USERNAME```
```spring.datasource.password=YOUR_DB_PASSWORD```

- マイグレーション、アプリケーションの起動
プロジェクトのルートディレクトリに移動します。
下記のコマンドを実行してデータベースのマイグレーション、アプリケーションの起動を実行します。

```mvn spring-boot:run```

- ブラウザへ移動

```http://localhost:8080/```
