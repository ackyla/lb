# -*- coding: utf-8 -*-
require 'spec_helper'

describe Territory do
  describe "Destroy" do
    before do
      @ter = build(:territory)
    end

    it "Destroy" do
      time = DateTime.now
      @ter.expire
      expired_time = @ter.expired_time
      expired_time != nil and time < expired_time
    end
  end
end
