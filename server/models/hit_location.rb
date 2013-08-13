class HitLocation < ActiveRecord::Base
  belongs_to :user
  belongs_to :room
  belongs_to :target, :class_name => "User"
end
