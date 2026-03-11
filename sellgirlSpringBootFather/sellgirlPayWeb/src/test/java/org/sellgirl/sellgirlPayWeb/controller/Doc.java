//package org.sellgirl.sellgirlPayWeb.controller;
//
//
//import io.github.swagger2markup.GroupBy;
//import io.github.swagger2markup.Language;
//import io.github.swagger2markup.Swagger2MarkupConfig;
//import io.github.swagger2markup.Swagger2MarkupConverter;
//import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
//import io.github.swagger2markup.markup.builder.MarkupLanguage;
//import org.junit.Test;
//import java.net.URL;
//import java.nio.file.Paths;
//
//public class Doc {
//
//    /**
//     * 生成文档的过程:
//     * 1.打包jar后用java -jar另外运行(因为此测试用例不会启动spring)
//     *   或在eclipse中运行项目调试
//     * 2.执行此测试用例
//     * 3.ok 测试通过
//     * 
//     * 1.生成AsciiDocs格式文档
//     * 2. 此项目 目录下 mvn asciidoctor:process-asciidoc
//     * 访问地址：http://localhost:9090/document/paths.html
//     * @throws Exception
//     */
//    @Test
//    public void generateAsciiDocs() throws Exception {
//        //    输出Ascii格式
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter
////        .from(new URL("http://localhost:9090/v2/api-docs"))
//        .from(new URL("http://localhost:28303/v2/api-docs"))
//                .withConfig(config)
//                .build()
//                .toFolder(Paths.get("./asciidoc"));
//    }
//
//    /**
//     * 生成Markdown格式文档
//     * @throws Exception
//     */
//    @Test
//    public void generateMarkdownDocs() throws Exception {
//        //    输出Markdown格式
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.MARKDOWN)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter.from(new URL("http://localhost:9090/v2/api-docs"))
//                .withConfig(config)
//                .build()
//                .toFolder(Paths.get("./docs/markdown/generated"));
//    }
//    /**
//     * 生成Confluence格式文档
//     * @throws Exception
//     */
//    @Test
//    public void generateConfluenceDocs() throws Exception {
//        //    输出Confluence使用的格式
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.CONFLUENCE_MARKUP)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter.from(new URL("http://localhost:8082/v2/api-docs"))
//                .withConfig(config)
//                .build()
//                .toFolder(Paths.get("./docs/confluence/generated"));
//    }
//
//    /**
//     * 生成AsciiDocs格式文档,并汇总成一个文件
//     * @throws Exception
//     */
//    @Test
//    public void generateAsciiDocsToFile() throws Exception {
//        //    输出Ascii到单文件
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter.from(new URL("http://localhost:8082/v2/api-docs"))
//                .withConfig(config)
//                .build()
//                .toFile(Paths.get("./docs/asciidoc/generated/all"));
//    }
//
//    /**
//     * 生成Markdown格式文档,并汇总成一个文件
//     * @throws Exception
//     */
//    @Test
//    public void generateMarkdownDocsToFile() throws Exception {
//        //    输出Markdown到单文件
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.MARKDOWN)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter.from(new URL("http://localhost:8082/v2/api-docs"))
//                .withConfig(config)
//                .build()
//                .toFile(Paths.get("./docs/markdown/generated/all"));
//    }
//
//}
