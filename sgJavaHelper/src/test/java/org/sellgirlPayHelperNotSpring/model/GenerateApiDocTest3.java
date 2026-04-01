package org.sellgirlPayHelperNotSpring.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * deepseek改进的版本, 存在错漏. 把优点合并到GenerateApiDocTest
 * 
 * @deprecated 我的GenerateApiDocTest版本已经超过此版本
 */
@Deprecated
public class GenerateApiDocTest3 {

//    @Test
    public static void generateHtml(String swaggerJson) throws Exception {
//        // 1. 获取 Swagger JSON
//        TestRestTemplate restTemplate = new TestRestTemplate();
//        ResponseEntity<String> response = restTemplate.getForEntity(
//                "http://localhost:8080/v2/api-docs", String.class);
//        String swaggerJson = response.getBody();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(swaggerJson);

        // 2. 构建 definitions 映射（用于查找模型字段）
        Map<String, JsonNode> definitions = new HashMap<>();
        JsonNode defs = root.path("definitions");
        if (defs.isObject()) {
            defs.fields().forEachRemaining(entry -> definitions.put(entry.getKey(), entry.getValue()));
        }

        // 3. 构建 HTML
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>API文档</title>");
        html.append("<style>");
        html.append("body{font-family: Arial, sans-serif; margin:20px;}");
        html.append(".api{margin-bottom:40px; border-bottom:2px solid #ddd; padding-bottom:20px;}");
        html.append("h2{color:#333;}");
        html.append("table{border-collapse:collapse; width:100%; margin:10px 0;}");
        html.append("th,td{border:1px solid #ddd; padding:8px; text-align:left;}");
        html.append("th{background-color:#f2f2f2;}");
        html.append(".example{background:#f9f9f9; padding:10px; border-left:4px solid #5cb85c; margin:10px 0; font-family: monospace;}");
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

                // 摘要和描述
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

                // ========== 关键改进：解析返回模型 ==========
                html.append("<h3>返回信息</h3>");
                JsonNode responses = operation.path("responses");

                // 检查是否返回 JSON 结构（有 schema 且指向 definition）
                JsonNode successResponse = responses.path("200");
                JsonNode schema = successResponse.path("schema");
                boolean hasJsonModel = false;

                if (!schema.isMissingNode()) {
                    String ref = schema.path("$ref").asText();
                    if (!ref.isEmpty()) {
                        // 从 $ref 中提取模型名，例如 "#/definitions/AbstractApiResult"
                        String modelName = ref.substring(ref.lastIndexOf('/') + 1);
                        JsonNode modelDef = definitions.get(modelName);
                        if (modelDef != null) {
                            hasJsonModel = true;
                            html.append("<p><strong>返回类型：</strong>").append(modelName).append(" (JSON)</p >");
                            html.append("<table><tr><th>字段</th><th>类型</th><th>说明</th></tr>");
                            JsonNode properties = modelDef.path("properties");
                            if (properties.isObject()) {
                                properties.fields().forEachRemaining(prop -> {
                                    String fieldName = prop.getKey();
                                    JsonNode fieldDef = prop.getValue();
                                    String fieldType = fieldDef.path("type").asText();
                                    if (fieldType.isEmpty()) {
                                        // 可能是引用类型
                                        fieldType = fieldDef.path("$ref").asText();
                                        if (fieldType.startsWith("#/definitions/"))
                                            fieldType = fieldType.substring(fieldType.lastIndexOf('/') + 1);
                                    }
                                    String fieldDesc = fieldDef.path("description").asText();
                                    html.append("<tr><td>").append(fieldName)
                                        .append("</td><td>").append(fieldType)
                                        .append("</td><td>").append(fieldDesc).append("</td></tr>");
                                });
                            }
                            html.append("</table>");
                        }
                    }
                }

                // 如果没找到 JSON 模型，则判断是否是 HTML 视图（produces 包含 text/html 或 */*）
                if (!hasJsonModel) {
                    String produces = operation.path("produces").toString();
                    if (produces.contains("text/html") || produces.contains("*/*")) {
                        html.append("<p><strong>返回类型：</strong>HTML 视图</p >");
                        // 展示 @ApiResponse 中的说明
                        if (responses.isObject() && responses.size() > 0) {
                            Iterator<Map.Entry<String, JsonNode>> respIter = responses.fields();
                            while (respIter.hasNext()) {
                                Map.Entry<String, JsonNode> entry = respIter.next();
                                String code = entry.getKey();
                                String msg = entry.getValue().path("description").asText();
                                if (!msg.isEmpty()) {
                                    html.append("<p><strong>").append(code).append("：</strong>").append(msg).append("</p >");
                                }
                            }
                        } else {
                            html.append("<p>💡 提示：请在控制器方法上添加 <code>@ApiResponse</code> 注解，说明成功/失败时的行为。</p >");
                        }
                    } else {
                        // 其他情况，简单展示响应码
                        html.append("<p><strong>HTTP 状态码说明</strong></p >");
                        if (responses.isObject() && responses.size() > 0) {
                            Iterator<Map.Entry<String, JsonNode>> respIter = responses.fields();
                            while (respIter.hasNext()) {
                                Map.Entry<String, JsonNode> entry = respIter.next();
                                String code = entry.getKey();
                                String msg = entry.getValue().path("description").asText();
                                html.append("<p><strong>").append(code).append("：</strong>").append(msg).append("</p >");
                            }
                        }
                    }
                }

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