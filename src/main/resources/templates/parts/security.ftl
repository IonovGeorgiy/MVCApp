<#--это все нужно что бы не показывать не зарегистрированному пользователю или не админу, то что видеть не нужно.-->
<#assign <#--используется для определения переменных внутри шаблонов Freemarker-->
    known = Session.SPRING_SECURITY_CONTEXT?? <#--проверяем существует ли в контексте необходимый обьект-->
  >

<#if known> <#--если сессия существует-->
    <#assign
    user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    name = user.getUsername()
    isAdmin = user.getAuthorities()?seq_contains('ADMIN')
<#--
    isAdmin = user.isAdmin()
-->
    >
    <#else>
    <#assign
        name = "unknown"
        isAdmin = false
        >
</#if>