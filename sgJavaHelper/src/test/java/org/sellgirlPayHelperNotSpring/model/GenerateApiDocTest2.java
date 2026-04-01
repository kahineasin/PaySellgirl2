package org.sellgirlPayHelperNotSpring.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@Deprecated
public class GenerateApiDocTest2 {

//    @Test
    public static void generateHtml(String swaggerJson) throws Exception {
//        // 1. 获取 Swagger JSON（可从本地服务获取，也可从文件读取）
//        TestRestTemplate restTemplate = new TestRestTemplate();
//        ResponseEntity<String> response = restTemplate.getForEntity(
//                "http://localhost:8080/v2/api-docs", String.class);
//        String swaggerJson = response.getBody();

        // 可选：保存 JSON 到文件以便调试
        Files.write(Paths.get("target/swagger.json"), swaggerJson.getBytes());

        // 2. 解析 JSON
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(swaggerJson);

        // 3. 构建 HTML
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>API文档</title>");
        html.append("<style>");
        html.append("body{font-family: Arial, sans-serif; margin:20px;}");
        html.append(".api{margin-bottom:30px; border-bottom:1px solid #ccc;}");
        html.append("h2{color:#333;}");
        html.append("table{border-collapse:collapse; width:100%; margin:10px 0;}");
        html.append("th,td{border:1px solid #ddd; padding:8px; text-align:left;}");
        html.append("th{background-color:#f2f2f2;}");
        html.append(".example{background:#f9f9f9; padding:10px; border-left:4px solid #5cb85c; margin:10px 0;}");
        html.append("</style></head><body>");

        // 添加基本信息
        JsonNode info = root.path("info");
        html.append("<h1>").append(info.path("title").asText("API文档")).append("</h1>");
        html.append("<p>").append(info.path("description").asText()).append("</p >");

        // 遍历所有路径
        JsonNode paths = root.path("paths");
        paths.fields().forEachRemaining(pathEntry -> {
            String path = pathEntry.getKey();
            JsonNode methods = pathEntry.getValue();

            methods.fields().forEachRemaining(methodEntry -> {
                String httpMethod = methodEntry.getKey().toUpperCase();
                JsonNode operation = methodEntry.getValue();

                html.append("<div class='api'>");
                html.append("<h2>").append(httpMethod).append(" ").append(path).append("</h2>");

                // 接口描述
                String summary = operation.path("summary").asText("");
                String description = operation.path("description").asText("");
                if (!summary.isEmpty()) html.append("<p><strong>摘要：</strong>").append(summary).append("</p >");
                if (!description.isEmpty()) html.append("<p><strong>说明：</strong>").append(description).append("</p >");

                // 请求参数
                JsonNode parameters = operation.path("parameters");
                if (parameters.isArray() && parameters.size() > 0) {
                    html.append("<h3>请求参数</h3>");
                    html.append("<table><tr><th>名称</th><th>位置</th><th>类型</th><th>必填</th><th>说明</th></tr>");
                    for (JsonNode param : parameters) {
                        String name = param.path("name").asText();
                        String in = param.path("in").asText();
                        String type = param.path("type").asText();
                        boolean required = param.path("required").asBoolean(false);
                        String desc = param.path("description").asText();
                        html.append("<tr>")
                            .append("<td>").append(name).append("</td>")
                            .append("<td>").append(in).append("</td>")
                            .append("<td>").append(type).append("</td>")
                            .append("<td>").append(required ? "是" : "否").append("</td>")
                            .append("<td>").append(desc).append("</td>")
                            .append("</tr>");
                    }
                    html.append("</table>");
                }

                // 请求示例（如果有）
                JsonNode examples = operation.path("x-examples"); // Swagger 2.0 默认没有，可自定义扩展
                // 也可从 parameters 的 example 字段取，这里简单处理：如果存在请求体，尝试生成示例
                // 由于我们针对表单/查询参数，这里略过，你可以自己完善

                // 响应信息（重点：针对返回 HTML 的接口）
                html.append("<h3>响应说明</h3>");
                JsonNode responses = operation.path("responses");
                if (responses.isObject() && responses.size() > 0) {
                    Iterator<Map.Entry<String, JsonNode>> responseFields = responses.fields();
                    while (responseFields.hasNext()) {
                        Map.Entry<String, JsonNode> entry = responseFields.next();
                        String statusCode = entry.getKey();
                        JsonNode response = entry.getValue();
                        String responseDesc = response.path("description").asText();
                        html.append("<p><strong>").append(statusCode).append("：</strong>").append(responseDesc).append("</p >");
                    }
                } else {
                    // 没有 @ApiResponse 注解时，根据返回类型给出默认提示
                    String produces = operation.path("produces").toString();
                    if (produces.contains("*/*") || produces.contains("text/html")) {
                        html.append("<p><strong>返回类型：</strong>HTML 视图</p >");
                        html.append("<p>💡 提示：请在控制器方法上添加 <code>@ApiResponse</code> 注解，以明确成功/失败时的行为。</p >");
                    } else {
                        html.append("<p><strong>返回类型：</strong>").append(produces).append("</p >");
                    }
                }

                // 补充模型数据说明（针对视图返回，你可以通过自定义注解或手动添加）
                // 这里提供一个可扩展点：如果你的控制器中有 @ApiModel 或自定义注解，可以解析出来

                html.append("</div>");
            });
        });

        html.append("</body></html>");

        // 4. 写入文件
        File output = new File("docs/api.html");
        output.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(output)) {
            writer.write(html.toString());
        }

        System.out.println("✅ 文档已生成：" + output.getAbsolutePath());
    }
}