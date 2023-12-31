import DefaultWrapper from "../ui/DefaultWrapper";
import GoogleLoginButtonImg from "../img/btn_google_signin_light_normal_web@2x.png";
import styled from "styled-components";
import VerticalAlignCenterWrapper from "../ui/VerticalAlignCenterWrapper";
import axios from "axios";
import {useEffect, useState} from "react";
import DefaultButton from "../ui/DefaultButton";
import WorkspaceSelector from "../component/home/WorkspaceSelector";
import {getMyUser} from "../api/user";
import {Environment} from "../api/Environment";

const Tab = styled.div`
    border: 0px solid gray;
    height: 300px;
    text-align:center;
`

const SocialLoginButton = styled.button`
    background: url(${GoogleLoginButtonImg});
    background-size:cover;
    width: 235px;
    height: 54px;
    border:0;
 
`



function HomePage() {

    const [isLogined,setIsLogined] = useState(false);
    const [userInfo,setUserInfo] = useState({});

    const loginCheck = () => {
        getMyUser().then((response) => {
            setIsLogined(true);
            setUserInfo(response);
        }).catch((error) => {
            setIsLogined(false);
            setUserInfo({});
        });
    }

    useEffect(() => {
       loginCheck();
    },[]);


    const logout = () => {
        axios.post("/logout").then((response) => {
            window.location.reload();
        });
    }


    function onLoginButtonClick() {
        console.log("onLoginButtonClick");
        window.location.href = "https://accounts.google.com/o/oauth2/auth/oauthchooseaccount?client_id=975667075932-1t71d0nrajvg1qpgev1biqtks45v2saf.apps.googleusercontent.com&redirect_uri="+Environment.OAUTH_LOGIN_REDIRECT_URL+"&response_type=code&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email%20https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile&service=lso&o2v=1&flowName=GeneralOAuthFlow";
    }

    let content = isLogined ?
        <Tab><h2>MySlack</h2><p>{userInfo.nickname} 회원님</p>
            <WorkspaceSelector />
            <DefaultButton title={"로그아웃"} onClick={logout} />
        </Tab>
        :
        <Tab className={"shadow-lg p-3 mb-5 bg-body-tertiary rounded"}>
            <h1>My Slack</h1>
            <h2>소셜 로그인</h2>
            <SocialLoginButton onClick={onLoginButtonClick}
                               alt=""/>
        </Tab>


    return (
        <VerticalAlignCenterWrapper>
            <DefaultWrapper>
                {content}
            </DefaultWrapper>
        </VerticalAlignCenterWrapper>
    );
}

export default HomePage;