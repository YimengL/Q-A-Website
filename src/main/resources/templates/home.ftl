<html>
    <body>
        <pre>
            ${value1}
            <#list colors as color>
                This is Color ${color?index}: ${color}, ${color?counter}
            </#list>

            <#list map?keys as key>
                Number: ${key}, Value: ${map[key]}
            </#list>

            User: ${user.name}, ${user.description}, ${user.getDescription()}

            <#assign title="nowcoder">
            Title: ${title}

            <#include "header.ftl">
        </pre>
    </body>
</html>
