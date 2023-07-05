

import styled from "styled-components";
import {useNavigate} from "react-router-dom";

const Wrapper = styled.div`
    width:1000px;
    height:917px;
    padding:32px 80px 64px;
    color:#D1D2D3;
`;

const Progress = styled.div`
    font-size:13px;
    margin-bottom:24px;
`;

const Title = styled.div`
    font-size:50px;
    font-weight:900;
    margin-bottom:8px;
`;

const Description = styled.div`
    font-size:15px;
    margin-bottom:24px;
`;

const MessageTextarea = styled.textarea`
    background-color:inherit;
    color:inherit;
    border:1px solid #515357;
    height:36px;
    font-size:15px;
    
    resize:none;
    
    &::placeholder{
        color:#7A7B7D;
        font-weight:900;
    }
    &:focus {
        background-color: #222529;
        color:white;
        border:0;
        //box-shadow:none;
    }
    
    margin-bottom:20px;
`;

const SubmitButton = styled.button`
    background-color:#35373B;
    color:#AAABAD;
    border:0;
    font-weight:900;
    width:200px;
    height:44px;
    font-size:18px;
    margin-top:48px;
    // 값이 입력되면 #4A154B 으로 색변경
`;

const AddNewEmailSpan = styled.span`
    color:#1D9BD1;
    
    &:hover {
        text-decoration:underline;
        cursor:pointer;
    }
`;

const FreeStart = styled.span`
    color:#1D9BD1;
     &:hover {
        text-decoration:underline;
        cursor:pointer;
    }
`;

const LaterSpan = styled.span`
    display:block;
    margin-top:20px;
    color:#AAABAD;
    font-weight:700;
    
    &:hover {
        text-decoration:bold;
        cursor:pointer;
    }
`;


function WorkspaceCreatedSection(props) {

    const navigate = useNavigate();

    const handleSubmit = () => {
        const uri = "/app/workspace/"+props.workspaceId;
        navigate(uri);
    }

    console.log(props.workspaceId)
    return (
        <Wrapper>
            <Progress>4/4단계</Progress>
            <Title>이제 Slack 워크스페이스가 준비되었습니다✨</Title>
            <Description>한 단계만 더 완료하면 프로모션을 사용하고 프리미엄 기능에 액세스할 수 있습니다. 허들을 사용하여 실시간으로 문제 해결을 시작하고, 오디오 및 비디오 클립으로 업무 효율성을 높이고, Slack Connect를 통해 외부 사람들과 안전하게 작업하려면 결제를 완료하세요.</Description>
            <form>
                <SubmitButton type="submit" disabled>결제 계속하기</SubmitButton>
            </form>
            <div style={{marginTop:20}}>
                <span>또는 <FreeStart onClick={handleSubmit} >제한된 무료 버전</FreeStart> 으로 시작할 수도 있습니다.</span>
            </div>
        </Wrapper>
    )
}

export default WorkspaceCreatedSection;