import React from "react";
import DefaultWrapper from "../ui/DefaultWrapper";
import GoogleLoginButtonImg from "../img/btn_google_signin_light_normal_web@2x.png";
function DevPage() {

  function onLoginButtonClick(){
    console.log("onLoginButtonClick");
    window.location.href = "https://accounts.google.com/o/oauth2/auth/oauthchooseaccount?client_id=975667075932-1t71d0nrajvg1qpgev1biqtks45v2saf.apps.googleusercontent.com&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Flogin%2Foauth2%2Fcode%2Fgoogle&response_type=code&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email%20https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile&service=lso&o2v=1&flowName=GeneralOAuthFlow";
  }


  return (
    <DefaultWrapper>
      <h1>Login</h1>
        <img onClick={onLoginButtonClick} style={{width:150}} src={GoogleLoginButtonImg} alt=""/>
        <h1>API</h1>

    </DefaultWrapper>
  );
}

export default DevPage;