class Room < ActiveRecord::Base
  has_many :users
  has_many :missions
  has_many :locations
end
