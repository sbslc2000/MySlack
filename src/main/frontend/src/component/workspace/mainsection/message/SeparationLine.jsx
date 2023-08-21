import styled from "styled-components";


const Wrapper = styled.div`
    height:40px;
    display:flex;
    align-items:center;
    justify-content: center;
    
`
const Line = styled.div`
    border-bottom:2px solid #35373B;
    width:765.5px;
    height:0px;
    top:20px;
`;

const SeparationLineButton = styled.button`
    background-color:inherit;
    border:1px solid gray;
    border-radius:10px;
    color:inherit;

    top:0px;
    right:830px;
    
    width:129px;
    height:28px;
    font-size:13px;
    font-weight:900;
`;
function SeparationLine(props) {
    return (
        <Wrapper>
            <SeparationLineButton>{props.date}</SeparationLineButton>
        </Wrapper>
    )
}

export default SeparationLine;