import React from "react";
import { render, fireEvent } from "@testing-library/react";
import FormInput from "./FormInput";

describe("layout", () => {
  it("has input item", () => {
    const { container } = render(<FormInput />);
    expect(container.querySelector("input")).toBeInTheDocument();
  });

  it("displays the label provided in props", () => {
    const { queryByText } = render(<FormInput label="test-label" />);
    expect(queryByText("test-label")).toBeInTheDocument();
  });

  it("does not display label when label is not provided in props", () => {
    const { container } = render(<FormInput />);
    expect(container.querySelector("label")).not.toBeInTheDocument();
  });

  it("has text type for input when type is not provided as prop", () => {
    const { container } = render(<FormInput />);
    expect(container.querySelector("input").type).toBe("text");
  });

  it("has password type for input when password type is provided as prop", () => {
    const { container } = render(<FormInput type="password" />);
    expect(container.querySelector("input").type).toBe("password");
  });

  it("displays placeholder when it is provided as prop", () => {
    const { container } = render(<FormInput placeholder="test-placeholder" />);
    expect(container.querySelector("input").placeholder).toBe(
      "test-placeholder"
    );
  });
  it("has value for input when it is provided as prop", () => {
    const { container } = render(<FormInput value="test-value" />);
    expect(container.querySelector("input").value).toBe("test-value");
  });

  it("has onChange callback when it is provided as prop", () => {
    const onChange = jest.fn();
    const { container } = render(<FormInput onChange={onChange} />);
    const input = container.querySelector("input");
    fireEvent.change(input, { target: { value: "new input" } });
    expect(onChange).toHaveBeenCalledTimes(1);
  });

  it("has default style when there is no validation error or success", () => {
    const { container } = render(<FormInput />);
    const input = container.querySelector("input");
    expect(input.classList.contains("valid")).toBe(false);
    expect(input.classList.contains("invalid")).toBe(false);
  });

  it("has error style when hasError property is true", () => {
    const { container } = render(<FormInput hasError={true} />);
    const input = container.querySelector("input");
    expect(input.classList.contains("invalid")).toBe(true);
  });

  it("displays the error text when it is provided", () => {
    const { queryByText } = render(
      <FormInput hasError={true} error="Cannot be null" />
    );
    expect(queryByText("Cannot be null")).toBeInTheDocument();
  });

  it("does not display the error text when hasError is not provided", () => {
    const { queryByText } = render(<FormInput error="Cannot be null" />);
    expect(queryByText("Cannot be null")).not.toBeInTheDocument();
  });
});
