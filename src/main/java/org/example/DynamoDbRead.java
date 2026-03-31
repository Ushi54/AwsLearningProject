package org.example;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import java.util.HashMap;
import java.util.Map;

public class DynamoDbRead {
    public static void main(String[] args) {
        // 1. クライアントの作成（※前回成功したリージョンに合わせてください）
        Region region = Region.AP_NORTHEAST_1;
        DynamoDbClient ddb = DynamoDbClient.builder()
                .region(region)
                .build();

        String tableName = "UserTable";

        // 2. 検索したいキー（UserId: user-001）を指定
        Map<String, AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put("UserId", AttributeValue.builder().s("user-001").build());

        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(keyToGet)
                .build();

        try {
            System.out.println("DynamoDBからデータを検索中...");
            // 3. データの取得を実行
            GetItemResponse response = ddb.getItem(request);

            if (response.hasItem()) {
                Map<String, AttributeValue> item = response.item();
                System.out.println("【取得成功】");
                System.out.println("名前: " + item.get("UserName").s());
                System.out.println("年齢: " + item.get("Age").n() + "歳");
            } else {
                System.out.println("該当するデータが見つかりませんでした。");
            }
        } catch (Exception e) {
            System.err.println("エラーが発生しました: " + e.getMessage());
        } finally {
            ddb.close();
        }
    }
}