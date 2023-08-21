import styled from "styled-components";

const Wrapper = styled.div`
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