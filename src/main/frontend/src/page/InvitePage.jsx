import WorkspaceFrame from "../component/workspace/WorkspaceFrame";
import styled from "styled-components";
import {useEffect, useState} from "react";
import axios from "axios";
import {useLocation, useNavigate} from "react-router-dom";
import {getUser} from "../api/user";
import {getWorkspace} from "../api/workspace";
import MyLogger from "../util/MyLogger";

const Wrapper = styled.div`
    padding-left:80px;
    padding-top:32px;
`;
const Title = styled.div`
    font-size:50px;
    font-weight:900;
`

const SubTitle = styled.div`
    font-size:30px;
    font-weight:900;
`;

const Link = styled.button`
    margin-top:30px;
    padding-left:0px;
    background-color:inherit;
    color:#1D9BD1;
    border:0;
    font-size:20px;
    font-weight:900;
`;

function InvitePage() {
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(true);
    const [content, setContent] = useState(<></>);

    const workspaceId = useLocation().pathname.split("/")[3];

    const onLoginButtonClick = () => {
        window.location.href = "https://accounts.google.com/o/oauth2/auth/oauthchooseaccount?client_id=975667075932-1t71d0nrajvg1qpgev1biqtks45v2saf.apps.googleusercontent.com&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Flogin%2Foauth2%2Fcode%2Fgoogle&response_type=code&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email%20https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile&service=lso&o2v=1&flowName=GeneralOAuthFlow";
    }

    useEffect(() => {
        Promise.allSettled([
            getUser(), getWorkspace(workspaceId)
        ]).then((results) => {

            setIsLoading(false);

            if(results[0].status === "rejected") {
                setContent(
                    <>
                        <Title>안녕하세요</Title>
                        <SubTitle>로그인 먼저 해주세요</SubTitle>
                        <Link onClick={onLoginButtonClick}>로그인하러가기</Link>
                    </>
                );
            } else if(results[1].status === "rejected") {
                setContent(
                    <>
                        <Title>오류</Title>
                        <SubTitle>워크스페이스를 찾을 수 없습니다.</SubTitle>
                    </>
                );
            } else {     //모두 통과시
                const workspace = results[1].value;

                /**
                 * todo:
                 * 여기에 워크스페이스 참가 요청 보내야함
                 */

                setContent(
                    <>
                        <Title>{workspace.name}에 참가합니다.</Title>
                        <SubTitle>MySlack은 협업공간을 제공합니다.</SubTitle>
                        <Link onClick={() => {navigate("/app/workspace/"+workspace.id); }}># {workspace.name} 바로가기</Link>
                    </>
                );
            }
        });
    }, []);

    return (
        <WorkspaceFrame>
            <Wrapper>
                {isLoading ?
                    <>
                        <Title>잠시만 기다려 주세요.</Title>
                        <SubTitle>MySlack은 협업공간을 제공합니다.</SubTitle>
                    </>
                    :
                    content}
            </Wrapper>
        </WorkspaceFrame>
    );
}

export default InvitePage;