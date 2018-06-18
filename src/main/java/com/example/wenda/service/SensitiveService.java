package com.example.wenda.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                addWord(lineTxt.trim());
            }
            read.close();
        }
        catch (Exception e) {
            logger.error("读取敏感词文件失败" + e.getMessage());
        }
    }

    // 增加关键词
    private void addWord(String lineTxt) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < lineTxt.length(); ++i) {
            Character c = lineTxt.charAt(i);

            if (isSymbol(c)) {
                continue;
            }

            TrieNode node = tempNode.getSubNode(c);

            if (node == null) {
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }

            tempNode = node;

            if (i == lineTxt.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    private class TrieNode {
        private boolean end = false;  // 是不是关键词结尾
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }

        public TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        public boolean isKeywordEnd() {
            return end;
        }

        public void setKeywordEnd(boolean end) {
            this.end = end;
        }
    }

    private TrieNode rootNode = new TrieNode();

    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 东亚文字
        return !Character.isLetterOrDigit(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    public String filter(String text) {
        if (StringUtils.isEmpty(text)) {
            return text;
        }

        StringBuilder result = new StringBuilder();

        String replacement = "***";
        TrieNode tempNode = rootNode;
        int begin = 0;
        int position = 0;

        while (position < text.length()) {
            char c = text.charAt(position);

            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }

            tempNode = tempNode.getSubNode(c);

            if (tempNode == null) {
                result.append(text.charAt(begin));
                position = begin + 1;
                begin = position;
                tempNode = rootNode;
            }
            else if (tempNode.isKeywordEnd()) {
                // 发现敏感词
                result.append(replacement);
                position += 1;
                begin = position;
                tempNode = rootNode;
            }
            else {
                ++position;
            }
        }

        result.append(text.substring(begin));
        return result.toString();
    }

    public static void main(String[] argv) {
        SensitiveService s = new SensitiveService();
        s.addWord("色情");
        s.addWord("赌博");
        System.out.println(s.filter("你好 色v情"));
    }
}
