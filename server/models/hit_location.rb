class HitLocation < ActiveRecord::Base
  has_one :user
  has_one :room
  has_one :target, :class_name => "User"
end
