package org.example;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;

import java.util.List;

public class SqsTest {
    public static void main(String[] args) {
        // 1. クライアントの作成
        Region region = Region.AP_NORTHEAST_1;

        // 【書き換えてください】ターミナルで取得したご自身のSQSのURL

        try (SqsClient sqs = SqsClient.builder()
                .region(region)
                .build()) {
            String queueUrl = "https://sqs.ap-northeast-1.amazonaws.com/115261254480/TestQueue";
            // 2. SQSにメッセージを送信する
            System.out.println("SQSにメッセージを送信しています...");
            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody("Javaから送信したテストメッセージです！")
                    .build();
            sqs.sendMessage(sendMsgRequest);
            System.out.println("送信完了！");

            // 3. SQSからメッセージを受信する
            System.out.println("\nSQSからメッセージを受信しています...");
            ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(1) // 最大1件取得
                    .build();

            List<Message> messages = sqs.receiveMessage(receiveRequest).messages();

            for (Message m : messages) {
                System.out.println("【受信したメッセージ】: " + m.body());

                // 4. 処理が終わったメッセージをキューから削除する（※SQSの重要ルール）
                DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .receiptHandle(m.receiptHandle())
                        .build();
                sqs.deleteMessage(deleteRequest);
                System.out.println("メッセージをキューから削除しました。");
            }
        } catch (Exception e) {
            System.err.println("エラーが発生しました: " + e.getMessage());
        }
    }
}