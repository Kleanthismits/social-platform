import React from "react";
import { render, fireEvent, waitFor } from "@testing-library/react";
import HomePage from "./HomePage";

describe("HomePage", () => {
  describe("layout", () => {
    it("has root page div", () => {
      const { queryByTestId } = render(<HomePage />);
      expect(queryByTestId("homepage")).toBeInTheDocument();
    });
  });
});
