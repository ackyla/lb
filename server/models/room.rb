class Room < ActiveRecord::Base
  has_many :users
  has_many :missions
  has_many :locations
  has_many :hit_locations
  has_one :owner, :class_name => "User"
end
