

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
    height:44px;
    font-size:20px;
    
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
function WorkspaceChannelFormSection(props) {

    const dtoContext = useContext(RequestDtoContext);
    const submitButtonColor = dtoContext.workspaceCreateRequestDto.channel ? "#4A154B" : "#35373B";
    const handleSubmit = (e) => {
        e.preventDefault();
        props.turnNext();
    }

    const formChangeHandler = (e) => {
        dtoContext.setWorkspaceCreateRequestDto({
            ...dtoContext.workspaceCreateRequestDto,
            channel: e.target.value
        });
    }
    return (
        <Wrapper>
            <Progress>2/4단계</Progress>
            <Title>현재 고객님의 팀은 어떤 일을 진행하고 계시나요?</Title>
            <Description>프로젝트, 캠페인, 이벤트 또는 성사하려는 거래 등 무엇이든 될 수 있습니다.</Description>
            <form>
                <div className="form-group">
                    <MessageTextarea onChange={formChangeHandler} placeholder="예: 4분기 예산, 가을 캠페인" className="form-control" id="textareaContent"
                                     rows="1"/>
                </div>
                <SubmitButton backgroundColor={submitButtonColor} onClick={handleSubmit} type="submit">다음</SubmitButton>
            </form>
        </Wrapper>
    )
}

export default WorkspaceChannelFormSection;