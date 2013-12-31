require 'spec_helper'

describe "CharactersController" do
  it "get list" do
    char = create(:character)
    get 'characters/list'
    expect(last_response).to be_ok
  end
end
