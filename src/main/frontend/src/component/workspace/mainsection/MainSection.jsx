import styled from "styled-components";

const Wrapper = styled.div`
    width:1660px;
    height:917px;
    background-color:#1A1D21;
`;

function MainSection(props) {
    return (
        <Wrapper>
            {props.children}
        </Wrapper>
    )
}

export default MainSection;