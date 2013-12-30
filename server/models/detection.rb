class Detection < ActiveRecord::Base
  belongs_to :location
  belongs_to :territory
  has_many :notifications
end
