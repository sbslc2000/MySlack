

import styled from "styled-components";
import {useContext} from "react";
import {RequestDtoContext} from "../../page/WorkspaceCreatePage";

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
    background-color:${props => props.backgroundColor};
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
function WorkspaceEmailFormSection(props) {

    let dtoContext = useContext(RequestDtoContext);
    const submitButtonColor = dtoContext.workspaceCreateRequestDto.invitees[0] ? "#4A154B" : "#35373B";


    const handleSubmit = (e) => {

        e.preventDefault();

        let invitees = [];

        const forms = document.getElementById("invitees").getElementsByTagName("textarea");
        for(let i = 0; i < forms.length; i++) {
            invitees.push(forms[i].value);
        }

        console.log(invitees);

        props.turnNext();
    }

    const onChangeHandler = (e) => {
        dtoContext.setWorkspaceCreateRequestDto(
            {
                ...dtoContext.workspaceCreateRequestDto,
                invitees: [e.target.value]
            }
        )
    }

    return (
        <Wrapper>
            <Progress>3/4단계</Progress>
            <Title>고객님이 워크스페이스이름에 대해 가장 이메일을 자주 보내는 대상은 누구인가요?</Title>
            <Description>MySlack에서 작업을 시작하려면 주기적으로 소통하는 직장 동료 몇 명을 추가하세요.</Description>
            <form>
                <div className="form-group" id="invitees">
                    <MessageTextarea onChange={onChangeHandler} placeholder="예: sbslc2000@gmail.com" className="form-control" id="textareaContent"
                                     rows="1"></MessageTextarea>
                </div>
                <div>
                    <AddNewEmailSpan>+ 다른 항목 추가</AddNewEmailSpan>
                </div>
                <SubmitButton backgroundColor={submitButtonColor} onClick={handleSubmit} type="submit">직장 동료 추가</SubmitButton>
            </form>
            <LaterSpan onClick={props.turnNext}>이 단계 건너뛰기</LaterSpan>

        </Wrapper>
    )
}

export default WorkspaceEmailFormSection;