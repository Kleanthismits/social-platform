import React from "react";
import { render, fireEvent, waitFor } from "@testing-library/react";
import UserPage from "./UserPage";

describe("UserPage", () => {
  describe("layout", () => {
    it("has root page div", () => {
      const { queryByTestId } = render(<UserPage />);
      expect(queryByTestId("userpage")).toBeInTheDocument();
    });
  });
});