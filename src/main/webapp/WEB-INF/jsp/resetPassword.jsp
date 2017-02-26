<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: hejiang
  Date: 14/12/14
  Time: 下午4:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>重设密码</title>
</head>

<script language="javascript">
  document.register.password.focus();
  function test(){
    var f = document.reset;
    var newPsw1 =f.newPassword.value;
    var newPsw2 = f.confirmPassword.value;
    if(newPsw1 == "" || newPsw2 == ""){
      alert("密码框不能为空!");
      f.newPassword.value='';
      f.confirmPassword.value='';
      f.newPassword.focus();
      return false;
    }
    if(newPsw1 != newPsw2){
      alert("两次密码输入不一致!");
      f.newPassword.value='';
      f.confirmPassword.value='';
      f.newPassword.focus();
      return false;
    }
    return true;
  }

</script>
<body>
<form method="post" action="<%=request.getContextPath()%>/user/reset" name="reset">
  New Password: <input name="newPassword" type="password" />
  <br/>
  Confirm Password: <input name="confirmPassword" type="password" />
  <br/>
  <input type="hidden" name="userName" value="${userName}">
  <input type="submit" value="Submit" onclick="return test()">
</form>
</body>
</html>
