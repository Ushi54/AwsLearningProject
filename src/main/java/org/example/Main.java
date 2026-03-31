package org.example;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;


public class Main {
    public static void main(String[] args) {
//     getTableList();
        insertData();
    }

    /**
     * awsのdynamodbからテーブル一覧を取得し、ログに出力するメソッド
     */
    static void getTableList(){
        // 1. AWSに接続するための「クライアント（窓口）」を作成
        // ※ターミナルで設定した東京リージョンを指定しています
        Region region = Region.AP_NORTHEAST_1;
        DynamoDbClient ddb = DynamoDbClient.builder()
                .region(region)
                .build();

        System.out.println("AWSに接続し、DynamoDBのテーブル一覧を取得中...");

        // 2. テーブル一覧を取得するリクエストを送信
        ListTablesResponse response = ddb.listTables();

        // 3. 取得したテーブル名を1つずつ画面に表示
        System.out.println("【取得結果】");
        response.tableNames().forEach(tableName -> {
            System.out.println("- " + tableName);
        });

        // 4. 使い終わったら窓口を閉じる
        ddb.close();
        System.out.println("処理が完了しました！");
    }

    /**
     *
     */
    static void insertData() {
        // 1. クライアント（窓口）の作成
        // ※前回 listTables に成功したリージョン（AP_NORTHEAST_1 または US_EAST_1）を指定してください
        Region region = Region.AP_NORTHEAST_1;
        DynamoDbClient ddb = DynamoDbClient.builder()
                .region(region)
                .build();

        // 操作するテーブル名
        String tableName = "UserTable";

        // 2. 書き込むデータの準備（Mapの中にデータを詰めていきます）
        Map<String, AttributeValue> itemValues = new HashMap<>();

        // パーティションキー（文字列：s）※設定したキー名と完全に一致させる必要があります
        itemValues.put("UserId", AttributeValue.builder().s("user-001").build());

        // 追加の属性データ（文字列：s, 数値：n）
        itemValues.put("UserName", AttributeValue.builder().s("Taro").build());
        itemValues.put("Age", AttributeValue.builder().n("25").build());

        // 3. 書き込みリクエストの作成
        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(itemValues)
                .build();

        try {
            // 4. データ書き込みの実行
            System.out.println("DynamoDBへデータを書き込んでいます...");
            ddb.putItem(request);
            System.out.println("データの書き込みに成功しました！");
        } catch (Exception e) {
            System.err.println("エラーが発生しました: " + e.getMessage());
        } finally {
            // 5. 使い終わったら窓口を閉じる
            ddb.close();
        }
    }
}
