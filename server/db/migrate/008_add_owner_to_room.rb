class AddOwnerToRoom < ActiveRecord::Migration
  def self.up
    change_table :rooms do |t|
      t.references :owner
    end
  end

  def self.down
    change_table :rooms do |t|
      t.remove :owner
    end
  end
end
