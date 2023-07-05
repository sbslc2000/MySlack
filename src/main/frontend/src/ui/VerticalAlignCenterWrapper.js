import styled from "styled-components";

const Wrapper = styled.div`
  display:flex;
  align-items: center;
  width:100%;
  height:100%;
`;

function VerticalAlignCenterWrapper(props) {

    return (
        <Wrapper>
            {props.children}
        </Wrapper>
    );
}

export default VerticalAlignCenterWrapper;