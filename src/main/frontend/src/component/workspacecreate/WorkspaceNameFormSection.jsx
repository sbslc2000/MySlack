

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
function WorkspaceNameFormSection(props) {

    let dtoContext = useContext(RequestDtoContext);
    const submitButtonColor = dtoContext.workspaceCreateRequestDto.name ? "#4A154B" : "#35373B";

    const handleSubmit = (e) => {

        e.preventDefault();
        props.turnNext();
    }

    const onChangeHandler = (e) => {
        console.log(e.target.value);
        dtoContext.setWorkspaceCreateRequestDto({...dtoContext.workspaceCreateRequestDto, name: e.target.value});
    }

    return (
        <Wrapper>
            <Progress>1/4단계</Progress>
            <Title>회사 또는 팀 이름이 어떻게 됩니까?</Title>
            <Description>MySlack 워크스페이스의 이름이 됩니다. 팀이 인식할 수 있는 이름을 입력하세요.</Description>
            <form>
                <div className="form-group">
                    <MessageTextarea onChange={onChangeHandler} placeholder="예: Acme 마케팅 혹은 Acme" className="form-control" id="textareaContent"
                                     rows="1"></MessageTextarea>
                </div>
                <SubmitButton backgroundColor={submitButtonColor} onClick={handleSubmit} type="submit">다음</SubmitButton>
            </form>
        </Wrapper>
    )
}

export default WorkspaceNameFormSection;